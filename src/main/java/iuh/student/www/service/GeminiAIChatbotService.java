package iuh.student.www.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.student.www.dto.ChatbotRequest;
import iuh.student.www.dto.ChatbotResponse;
import iuh.student.www.entity.Order;
import iuh.student.www.entity.Product;
import iuh.student.www.entity.User;
import iuh.student.www.repository.OrderRepository;
import iuh.student.www.repository.ProductRepository;
import iuh.student.www.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gemini AI Chatbot Service
 * Sử dụng Gemini 2.0 Flash để quản lý thông tin người dùng, đơn hàng
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAIChatbotService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-2.0-flash-exp}")
    private String geminiModel;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    private final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Xử lý chat request
     */
    public ChatbotResponse chat(ChatbotRequest request) {
        try {
            log.info("Processing chatbot request - Message: {}, UserId: {}", request.getMessage(), request.getUserId());

            // Lấy context data dựa trên userId
            String contextData = buildContextData(request.getUserId(), request.getContextType());

            // Tạo prompt cho Gemini
            String prompt = buildPrompt(request.getMessage(), contextData);

            // Gọi Gemini API
            String aiResponse = callGeminiAPI(prompt);

            // Parse và format response
            return ChatbotResponse.builder()
                    .message(aiResponse)
                    .sessionId(request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString())
                    .success(true)
                    .suggestedActions(getSuggestedActions(request.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error processing chatbot request", e);
            return ChatbotResponse.builder()
                    .message("Xin lỗi, tôi đang gặp sự cố. Vui lòng thử lại sau.")
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    /**
     * Build context data cho AI
     */
    private String buildContextData(Long userId, String contextType) {
        StringBuilder context = new StringBuilder();

        if (userId != null) {
            // Lấy thông tin user
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.getPresent();
                context.append("## THÔNG TIN NGƯỜI DÙNG\n");
                context.append("- Tên: ").append(user.getName()).append("\n");
                context.append("- Email: ").append(user.getEmail()).append("\n");
                context.append("- Vai trò: ").append(user.getRole()).append("\n\n");

                // Lấy đơn hàng của user
                List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
                if (!orders.isEmpty()) {
                    context.append("## ĐƠN HÀNG\n");
                    context.append("Tổng số đơn hàng: ").append(orders.size()).append("\n");

                    // Lấy 5 đơn hàng gần nhất
                    orders.stream().limit(5).forEach(order -> {
                        context.append("\n### Đơn hàng #").append(order.getId()).append("\n");
                        context.append("- Ngày đặt: ").append(order.getOrderDate()).append("\n");
                        context.append("- Trạng thái: ").append(order.getStatus()).append("\n");
                        context.append("- Tổng tiền: ").append(String.format("%,d", order.getTotalAmount().intValue())).append("₫\n");
                        context.append("- Phương thức thanh toán: ").append(order.getPaymentMethod() != null ? order.getPaymentMethod() : "COD").append("\n");
                        context.append("- Sản phẩm: ").append(order.getOrderDetails().stream()
                                .map(detail -> detail.getProduct().getName() + " (x" + detail.getQuantity() + ")")
                                .collect(Collectors.joining(", "))).append("\n");
                    });
                }
            }
        }

        // Thêm thông tin sản phẩm nếu contextType là "product"
        if ("product".equals(contextType)) {
            List<Product> products = productRepository.findAll();
            context.append("\n## SẢN PHẨM CÓ SẴN\n");
            products.stream().limit(10).forEach(product -> {
                context.append("- ").append(product.getName())
                        .append(" (").append(String.format("%,d", product.getPrice().intValue())).append("₫")
                        .append(") - Còn: ").append(product.getQuantityInStock()).append("\n");
            });
        }

        return context.toString();
    }

    /**
     * Build prompt cho Gemini AI
     */
    private String buildPrompt(String userMessage, String contextData) {
        return String.format("""
                Bạn là trợ lý AI thông minh của Shop Mẹ và Bé - một cửa hàng bán đồ cho mẹ và bé.

                NHIỆM VỤ:
                - Trả lời câu hỏi của khách hàng về đơn hàng, sản phẩm, thông tin cá nhân
                - Cung cấp thông tin chính xác dựa trên dữ liệu
                - Gợi ý sản phẩm phù hợp
                - Hướng dẫn khách hàng thao tác trên website
                - Giải đáp thắc mắc về chính sách, vận chuyển, thanh toán

                QUY TẮC:
                - Trả lời bằng tiếng Việt thân thiện, lịch sự
                - Nếu không có thông tin, nói rõ và gợi ý liên hệ support
                - Không tự ý tạo thông tin không có trong dữ liệu
                - Sử dụng emoji phù hợp để thân thiện hơn (🍼, 👶, 💕, ✅, 📦)
                - Trả lời ngắn gọn, dễ hiểu

                DỮ LIỆU NGƯỜI DÙNG:
                %s

                CÂU HỎI: %s

                TRẢ LỜI:
                """, contextData, userMessage);
    }

    /**
     * Gọi Gemini API
     */
    private String callGeminiAPI(String prompt) throws IOException {
        String url = String.format(GEMINI_API_URL, geminiModel, geminiApiKey);

        // Tạo request body theo Gemini API format
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        // Safety settings
        List<Map<String, String>> safetySettings = new ArrayList<>();
        safetySettings.add(Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_NONE"));
        safetySettings.add(Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_NONE"));
        safetySettings.add(Map.of("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT", "threshold", "BLOCK_NONE"));
        safetySettings.add(Map.of("category", "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold", "BLOCK_NONE"));
        requestBody.put("safetySettings", safetySettings);

        // Generation config
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 1024);
        requestBody.put("generationConfig", generationConfig);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .build();

        log.debug("Calling Gemini API with prompt length: {}", prompt.length());

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                log.error("Gemini API error: {} - {}", response.code(), errorBody);
                throw new IOException("Gemini API call failed: " + response.code());
            }

            String responseBody = response.body().string();
            log.debug("Gemini API response: {}", responseBody);

            // Parse response
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");

                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).get("text");
                }
            }

            return "Xin lỗi, tôi không thể tạo phản hồi lúc này.";
        }
    }

    /**
     * Lấy suggested actions dựa trên tin nhắn
     */
    private List<String> getSuggestedActions(String message) {
        List<String> actions = new ArrayList<>();

        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("đơn hàng") || lowerMessage.contains("order")) {
            actions.add("Xem tất cả đơn hàng");
            actions.add("Tra cứu đơn hàng");
        }

        if (lowerMessage.contains("sản phẩm") || lowerMessage.contains("product")) {
            actions.add("Xem sản phẩm mới");
            actions.add("Sản phẩm khuyến mãi");
        }

        if (lowerMessage.contains("thanh toán") || lowerMessage.contains("payment")) {
            actions.add("Hướng dẫn thanh toán");
            actions.add("Phương thức thanh toán");
        }

        if (actions.isEmpty()) {
            actions.add("Xem đơn hàng");
            actions.add("Xem sản phẩm");
            actions.add("Liên hệ hỗ trợ");
        }

        return actions;
    }
}
