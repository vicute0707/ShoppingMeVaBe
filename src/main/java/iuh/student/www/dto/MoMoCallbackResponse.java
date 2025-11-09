package iuh.student.www.dto;

import lombok.*;

/**
 * MoMo Callback Response DTO
 * Response nhận từ MoMo sau khi thanh toán
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoMoCallbackResponse {

    private String partnerCode;
    private String orderId;
    private String requestId;
    private Long amount;
    private String orderInfo;
    private String orderType;
    private Long transId;
    private Integer resultCode;
    private String message;
    private String payType;
    private Long responseTime;
    private String extraData;
    private String signature;
}
