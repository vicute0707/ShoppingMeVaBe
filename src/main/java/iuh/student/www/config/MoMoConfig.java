package iuh.student.www.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình MoMo Payment Gateway
 * Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute
 */
@Configuration
@ConfigurationProperties(prefix = "momo")
@Getter
@Setter
public class MoMoConfig {

    /**
     * MoMo API endpoint
     */
    private String endpoint;

    /**
     * Partner Code được cung cấp bởi MoMo
     */
    private String partnerCode;

    /**
     * Access Key được cung cấp bởi MoMo
     */
    private String accessKey;

    /**
     * Secret Key để tạo chữ ký
     */
    private String secretKey;

    /**
     * URL callback sau khi thanh toán
     */
    private String redirectUrl;

    /**
     * URL nhận thông báo IPN (Instant Payment Notification)
     */
    private String ipnUrl;

    /**
     * Loại request (captureWallet, payWithATM, payWithCC)
     */
    private String requestType;
}
