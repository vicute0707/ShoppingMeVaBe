package iuh.student.www.dto;

import lombok.*;

/**
 * DTO cho chatbot request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotRequest {

    /**
     * Tin nhắn từ user
     */
    private String message;

    /**
     * ID của user (optional - để AI biết context)
     */
    private Long userId;

    /**
     * Session ID để track conversation
     */
    private String sessionId;

    /**
     * Context type: "order", "product", "profile", "general"
     */
    private String contextType;
}
