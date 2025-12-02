package iuh.student.www.dto;

import lombok.*;

/**
 * DTO cho chatbot response
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotResponse {

    /**
     * Phản hồi từ AI
     */
    private String message;

    /**
     * Session ID
     */
    private String sessionId;

    /**
     * Suggested actions (nếu có)
     */
    private java.util.List<String> suggestedActions;

    /**
     * Related data (orders, products, etc.)
     */
    private Object relatedData;

    /**
     * Success status
     */
    private boolean success;

    /**
     * Error message (if any)
     */
    private String error;
}
