package iuh.student.www.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.student.www.config.MoMoConfig;
import iuh.student.www.dto.MoMoCallbackResponse;
import iuh.student.www.dto.MoMoPaymentRequest;
import iuh.student.www.dto.MoMoPaymentResponse;
import iuh.student.www.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * MoMo Payment Service
 * Service xử lý thanh toán qua MoMo
 * Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute 🍼👶
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MoMoService {

    private final MoMoConfig moMoConfig;
    private final ObjectMapper objectMapper;

    /**
     * Tạo request thanh toán MoMo
     *
     * @param order Đơn hàng cần thanh toán
     * @return MoMoPaymentResponse chứa payUrl để redirect
     */
    public MoMoPaymentResponse createPayment(Order order) {
        try {
            // Tạo requestId và orderId duy nhất
            String requestId = UUID.randomUUID().toString();
            String orderId = "MOMOBE" + order.getId() + "_" + System.currentTimeMillis();

            // Tạo thông tin đơn hàng
            String orderInfo = "Thanh toán đơn hàng Mẹ và Bé #" + order.getId();

            // Số tiền (đơn vị VNĐ)
            Long amount = order.getTotalAmount().longValue();

            // Lưu user email vào extraData để restore session sau khi thanh toán
            // Encode Base64 để tránh lỗi với các ký tự đặc biệt
            String userEmail = order.getUser().getEmail();
            String extraData = java.util.Base64.getEncoder().encodeToString(userEmail.getBytes(StandardCharsets.UTF_8));

            log.info("Creating payment for user: {} (encoded in extraData)", userEmail);

            // Tạo raw signature
            String rawSignature = "accessKey=" + moMoConfig.getAccessKey() +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + moMoConfig.getIpnUrl() +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + moMoConfig.getPartnerCode() +
                    "&redirectUrl=" + moMoConfig.getRedirectUrl() +
                    "&requestId=" + requestId +
                    "&requestType=" + moMoConfig.getRequestType();

            log.info("Raw signature: {}", rawSignature);

            // Tạo signature
            String signature = generateSignature(rawSignature, moMoConfig.getSecretKey());

            // Tạo request body
            MoMoPaymentRequest request = MoMoPaymentRequest.builder()
                    .partnerCode(moMoConfig.getPartnerCode())
                    .partnerName("Cửa Hàng Mẹ và Bé")
                    .storeId("ShopMomBaby")
                    .requestId(requestId)
                    .amount(amount)
                    .orderId(orderId)
                    .orderInfo(orderInfo)
                    .redirectUrl(moMoConfig.getRedirectUrl())
                    .ipnUrl(moMoConfig.getIpnUrl())
                    .lang("vi")
                    .extraData(extraData)
                    .requestType(moMoConfig.getRequestType())
                    .signature(signature)
                    .autoCapture(true)
                    .build();

            log.info("MoMo Payment Request: {}", objectMapper.writeValueAsString(request));

            // Gọi API MoMo
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(moMoConfig.getEndpoint()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            log.info("MoMo Response Status: {}", response.statusCode());
            log.info("MoMo Response Body: {}", response.body());

            // Parse response
            MoMoPaymentResponse paymentResponse = objectMapper.readValue(response.body(), MoMoPaymentResponse.class);

            if (paymentResponse.getResultCode() == 0) {
                log.info("MoMo payment created successfully. PayUrl: {}", paymentResponse.getPayUrl());
            } else {
                log.error("MoMo payment failed: {} - {}", paymentResponse.getResultCode(), paymentResponse.getMessage());
            }

            return paymentResponse;

        } catch (Exception e) {
            log.error("Error creating MoMo payment", e);
            return MoMoPaymentResponse.builder()
                    .resultCode(-1)
                    .message("Lỗi khi tạo thanh toán: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Xác thực callback từ MoMo
     *
     * @param callback Callback response từ MoMo
     * @return true nếu signature hợp lệ
     */
    public boolean verifyCallback(MoMoCallbackResponse callback) {
        try {
            String rawSignature = "accessKey=" + moMoConfig.getAccessKey() +
                    "&amount=" + callback.getAmount() +
                    "&extraData=" + (callback.getExtraData() != null ? callback.getExtraData() : "") +
                    "&message=" + callback.getMessage() +
                    "&orderId=" + callback.getOrderId() +
                    "&orderInfo=" + callback.getOrderInfo() +
                    "&orderType=" + callback.getOrderType() +
                    "&partnerCode=" + callback.getPartnerCode() +
                    "&payType=" + callback.getPayType() +
                    "&requestId=" + callback.getRequestId() +
                    "&responseTime=" + callback.getResponseTime() +
                    "&resultCode=" + callback.getResultCode() +
                    "&transId=" + callback.getTransId();

            String signature = generateSignature(rawSignature, moMoConfig.getSecretKey());

            log.info("Generated signature: {}", signature);
            log.info("Callback signature: {}", callback.getSignature());

            return signature.equals(callback.getSignature());

        } catch (Exception e) {
            log.error("Error verifying MoMo callback", e);
            return false;
        }
    }

    /**
     * Tạo chữ ký HMAC SHA256
     *
     * @param data      Dữ liệu cần ký
     * @param secretKey Secret key
     * @return Chữ ký dạng hex string
     */
    private String generateSignature(String data, String secretKey) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Lấy order ID từ MoMo order ID
     * Format: MOMOBE{orderId}_{timestamp}
     */
    public Long extractOrderId(String momoOrderId) {
        try {
            // MOMOBE123_1234567890 -> 123
            String orderIdPart = momoOrderId.substring(6, momoOrderId.indexOf("_"));
            return Long.parseLong(orderIdPart);
        } catch (Exception e) {
            log.error("Error extracting order ID from: {}", momoOrderId, e);
            return null;
        }
    }

    /**
     * Decode user email từ extraData
     * extraData là Base64 encoded user email
     */
    public String extractUserEmail(String extraData) {
        try {
            if (extraData == null || extraData.isEmpty()) {
                return null;
            }
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(extraData);
            String userEmail = new String(decodedBytes, StandardCharsets.UTF_8);
            log.info("Extracted user email from extraData: {}", userEmail);
            return userEmail;
        } catch (Exception e) {
            log.error("Error extracting user email from extraData: {}", extraData, e);
            return null;
        }
    }
}
