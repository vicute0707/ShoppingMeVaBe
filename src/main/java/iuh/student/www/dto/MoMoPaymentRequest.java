package iuh.student.www.dto;

import lombok.*;

/**
 * MoMo Payment Request DTO
 * Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoMoPaymentRequest {

    private String partnerCode;
    private String partnerName;
    private String storeId;
    private String requestId;
    private Long amount;
    private String orderId;
    private String orderInfo;
    private String redirectUrl;
    private String ipnUrl;
    private String lang;
    private String extraData;
    private String requestType;
    private String signature;
    private Boolean autoCapture;
}
