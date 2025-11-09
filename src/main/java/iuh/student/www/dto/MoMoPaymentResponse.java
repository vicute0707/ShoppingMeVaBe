package iuh.student.www.dto;

import lombok.*;

/**
 * MoMo Payment Response DTO
 * Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoMoPaymentResponse {

    private String partnerCode;
    private String requestId;
    private String orderId;
    private Long amount;
    private Long responseTime;
    private String message;
    private Integer resultCode;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
    private String deeplinkMiniApp;
}
