package iuh.student.www.controller;

import iuh.student.www.dto.ChatbotRequest;
import iuh.student.www.dto.ChatbotResponse;
import iuh.student.www.service.GeminiAIChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Chatbot Controller
 * REST API cho AI Chatbot
 */
@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
public class ChatbotController {

    private final GeminiAIChatbotService geminiAIChatbotService;

    /**
     * Chat endpoint - Public (guest có thể chat)
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatbotResponse> chat(
            @RequestBody ChatbotRequest request,
            Authentication authentication) {

        log.info("Received chat request: {}", request.getMessage());

        // Nếu user đã đăng nhập, lấy userId từ authentication
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            log.info("Chat request from authenticated user: {}", email);
            // TODO: Lấy userId từ email nếu cần
        }

        ChatbotResponse response = geminiAIChatbotService.chat(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chatbot is running!");
    }
}
