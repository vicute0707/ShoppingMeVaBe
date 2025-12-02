<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- AI Chatbot Widget -->
<div id="chatbot-widget">
    <!-- Chatbot Button -->
    <button id="chatbot-toggle" class="chatbot-toggle" title="Trợ lý AI">
        <i class="fas fa-comments"></i>
        <span class="chatbot-badge">AI</span>
    </button>

    <!-- Chatbot Window -->
    <div id="chatbot-window" class="chatbot-window" style="display: none;">
        <!-- Header -->
        <div class="chatbot-header">
            <div class="chatbot-header-content">
                <div class="chatbot-avatar">
                    <i class="fas fa-robot"></i>
                </div>
                <div class="chatbot-header-text">
                    <h4>Trợ lý AI 🤖</h4>
                    <small class="chatbot-status">
                        <span class="status-dot"></span> Đang hoạt động
                    </small>
                </div>
            </div>
            <button id="chatbot-close" class="chatbot-close">
                <i class="fas fa-times"></i>
            </button>
        </div>

        <!-- Messages Area -->
        <div id="chatbot-messages" class="chatbot-messages">
            <!-- Welcome message -->
            <div class="chatbot-message bot-message">
                <div class="message-avatar">
                    <i class="fas fa-robot"></i>
                </div>
                <div class="message-content">
                    <div class="message-bubble">
                        Xin chào! 👋<br>
                        Tôi là trợ lý AI của Shop Mẹ và Bé.<br>
                        Tôi có thể giúp bạn:
                        <ul>
                            <li>📦 Tra cứu đơn hàng</li>
                            <li>🛍️ Tìm kiếm sản phẩm</li>
                            <li>👤 Quản lý thông tin cá nhân</li>
                            <li>💬 Giải đáp thắc mắc</li>
                        </ul>
                        Bạn cần giúp gì không? 😊
                    </div>
                    <div class="message-time">Bây giờ</div>
                </div>
            </div>
        </div>

        <!-- Suggested Actions -->
        <div id="chatbot-suggestions" class="chatbot-suggestions">
            <button class="suggestion-btn" data-message="Đơn hàng của tôi">
                📦 Đơn hàng của tôi
            </button>
            <button class="suggestion-btn" data-message="Sản phẩm mới">
                🛍️ Sản phẩm mới
            </button>
            <button class="suggestion-btn" data-message="Hướng dẫn thanh toán">
                💳 Hướng dẫn thanh toán
            </button>
        </div>

        <!-- Input Area -->
        <div class="chatbot-input">
            <textarea id="chatbot-message-input"
                      placeholder="Nhập tin nhắn..."
                      rows="1"
                      maxlength="500"></textarea>
            <button id="chatbot-send" class="chatbot-send-btn" title="Gửi">
                <i class="fas fa-paper-plane"></i>
            </button>
        </div>

        <!-- Typing Indicator -->
        <div id="chatbot-typing" class="chatbot-typing" style="display: none;">
            <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
            </div>
        </div>
    </div>
</div>

<!-- Chatbot CSS -->
<style>
    /* Chatbot Widget */
    #chatbot-widget {
        position: fixed;
        bottom: 20px;
        right: 20px;
        z-index: 9999;
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    }

    /* Toggle Button */
    .chatbot-toggle {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 24px;
        transition: all 0.3s ease;
        position: relative;
        animation: pulse 2s infinite;
    }

    .chatbot-toggle:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
    }

    .chatbot-badge {
        position: absolute;
        top: -5px;
        right: -5px;
        background: #ff4757;
        color: white;
        font-size: 10px;
        font-weight: bold;
        padding: 2px 6px;
        border-radius: 10px;
        border: 2px solid white;
    }

    @keyframes pulse {
        0%, 100% {
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        50% {
            box-shadow: 0 4px 20px rgba(102, 126, 234, 0.8);
        }
    }

    /* Chatbot Window */
    .chatbot-window {
        width: 380px;
        height: 600px;
        background: white;
        border-radius: 16px;
        box-shadow: 0 8px 40px rgba(0, 0, 0, 0.15);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        animation: slideUp 0.3s ease;
    }

    @keyframes slideUp {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    /* Header */
    .chatbot-header {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .chatbot-header-content {
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .chatbot-avatar {
        width: 40px;
        height: 40px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
    }

    .chatbot-header-text h4 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
    }

    .chatbot-header-text small {
        font-size: 12px;
        opacity: 0.9;
        display: flex;
        align-items: center;
        gap: 6px;
    }

    .status-dot {
        width: 8px;
        height: 8px;
        background: #2ecc71;
        border-radius: 50%;
        display: inline-block;
        animation: blink 2s infinite;
    }

    @keyframes blink {
        0%, 100% { opacity: 1; }
        50% { opacity: 0.5; }
    }

    .chatbot-close {
        background: rgba(255, 255, 255, 0.2);
        border: none;
        color: white;
        width: 32px;
        height: 32px;
        border-radius: 50%;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: background 0.3s;
    }

    .chatbot-close:hover {
        background: rgba(255, 255, 255, 0.3);
    }

    /* Messages Area */
    .chatbot-messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        background: #f8f9fa;
        display: flex;
        flex-direction: column;
        gap: 16px;
    }

    .chatbot-messages::-webkit-scrollbar {
        width: 6px;
    }

    .chatbot-messages::-webkit-scrollbar-thumb {
        background: #cbd5e0;
        border-radius: 3px;
    }

    /* Message */
    .chatbot-message {
        display: flex;
        gap: 10px;
        animation: messageSlide 0.3s ease;
    }

    @keyframes messageSlide {
        from {
            opacity: 0;
            transform: translateY(10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .user-message {
        flex-direction: row-reverse;
    }

    .message-avatar {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        flex-shrink: 0;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
    }

    .user-message .message-avatar {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    .message-content {
        display: flex;
        flex-direction: column;
        gap: 4px;
        max-width: 70%;
    }

    .message-bubble {
        padding: 12px 16px;
        border-radius: 16px;
        background: white;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        word-wrap: break-word;
        line-height: 1.5;
    }

    .user-message .message-bubble {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        border-bottom-right-radius: 4px;
    }

    .bot-message .message-bubble {
        border-bottom-left-radius: 4px;
    }

    .message-bubble ul {
        margin: 8px 0;
        padding-left: 20px;
    }

    .message-bubble li {
        margin: 4px 0;
    }

    .message-time {
        font-size: 11px;
        color: #718096;
        padding: 0 8px;
    }

    /* Suggestions */
    .chatbot-suggestions {
        padding: 12px 20px;
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        background: white;
        border-top: 1px solid #e2e8f0;
    }

    .suggestion-btn {
        padding: 8px 16px;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        background: white;
        cursor: pointer;
        font-size: 13px;
        transition: all 0.3s;
        color: #4a5568;
    }

    .suggestion-btn:hover {
        background: #667eea;
        color: white;
        border-color: #667eea;
        transform: translateY(-2px);
    }

    /* Input Area */
    .chatbot-input {
        padding: 16px 20px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        gap: 12px;
        align-items: flex-end;
    }

    #chatbot-message-input {
        flex: 1;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        padding: 10px 16px;
        resize: none;
        font-size: 14px;
        font-family: inherit;
        max-height: 100px;
        outline: none;
        transition: border-color 0.3s;
    }

    #chatbot-message-input:focus {
        border-color: #667eea;
    }

    .chatbot-send-btn {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        color: white;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: transform 0.3s;
        flex-shrink: 0;
    }

    .chatbot-send-btn:hover:not(:disabled) {
        transform: scale(1.1);
    }

    .chatbot-send-btn:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }

    /* Typing Indicator */
    .chatbot-typing {
        padding: 12px 20px;
        background: white;
        border-top: 1px solid #e2e8f0;
    }

    .typing-indicator {
        display: flex;
        gap: 4px;
        padding: 12px;
        background: #f7fafc;
        border-radius: 16px;
        width: fit-content;
    }

    .typing-indicator span {
        width: 8px;
        height: 8px;
        background: #cbd5e0;
        border-radius: 50%;
        animation: typing 1.4s infinite;
    }

    .typing-indicator span:nth-child(2) {
        animation-delay: 0.2s;
    }

    .typing-indicator span:nth-child(3) {
        animation-delay: 0.4s;
    }

    @keyframes typing {
        0%, 60%, 100% {
            transform: translateY(0);
            background: #cbd5e0;
        }
        30% {
            transform: translateY(-10px);
            background: #667eea;
        }
    }

    /* Mobile Responsive */
    @media (max-width: 768px) {
        .chatbot-window {
            width: 100vw;
            height: 100vh;
            border-radius: 0;
            position: fixed;
            bottom: 0;
            right: 0;
        }

        #chatbot-widget {
            bottom: 10px;
            right: 10px;
        }
    }
</style>

<!-- Chatbot JavaScript -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const toggleBtn = document.getElementById('chatbot-toggle');
    const closeBtn = document.getElementById('chatbot-close');
    const chatWindow = document.getElementById('chatbot-window');
    const messagesContainer = document.getElementById('chatbot-messages');
    const messageInput = document.getElementById('chatbot-message-input');
    const sendBtn = document.getElementById('chatbot-send');
    const typingIndicator = document.getElementById('chatbot-typing');
    const suggestionsContainer = document.getElementById('chatbot-suggestions');

    let sessionId = generateSessionId();

    // Toggle chatbot window
    toggleBtn.addEventListener('click', function() {
        const isVisible = chatWindow.style.display === 'block';
        chatWindow.style.display = isVisible ? 'none' : 'block';
        if (!isVisible) {
            messageInput.focus();
        }
    });

    closeBtn.addEventListener('click', function() {
        chatWindow.style.display = 'none';
    });

    // Send message
    sendBtn.addEventListener('click', sendMessage);
    messageInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    // Suggestion buttons
    document.querySelectorAll('.suggestion-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const message = this.getAttribute('data-message');
            messageInput.value = message;
            sendMessage();
        });
    });

    // Auto-resize textarea
    messageInput.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = Math.min(this.scrollHeight, 100) + 'px';
    });

    function sendMessage() {
        const message = messageInput.value.trim();
        if (!message) return;

        // Add user message to chat
        addMessage(message, 'user');
        messageInput.value = '';
        messageInput.style.height = 'auto';

        // Show typing indicator
        typingIndicator.style.display = 'block';
        sendBtn.disabled = true;

        // Call chatbot API
        fetch('/api/chatbot/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                message: message,
                sessionId: sessionId,
                userId: getUserId(),
                contextType: 'general'
            })
        })
        .then(response => response.json())
        .then(data => {
            // Hide typing indicator
            typingIndicator.style.display = 'none';
            sendBtn.disabled = false;

            // Add bot response
            if (data.success) {
                addMessage(data.message, 'bot');

                // Update suggested actions
                if (data.suggestedActions && data.suggestedActions.length > 0) {
                    updateSuggestions(data.suggestedActions);
                }
            } else {
                addMessage('Xin lỗi, có lỗi xảy ra. Vui lòng thử lại sau.', 'bot');
            }
        })
        .catch(error => {
            console.error('Chatbot error:', error);
            typingIndicator.style.display = 'none';
            sendBtn.disabled = false;
            addMessage('Xin lỗi, không thể kết nối đến server. Vui lòng thử lại sau.', 'bot');
        });
    }

    function addMessage(text, type) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `chatbot-message ${type}-message`;

        const avatar = document.createElement('div');
        avatar.className = 'message-avatar';
        avatar.innerHTML = type === 'bot' ? '<i class="fas fa-robot"></i>' : '<i class="fas fa-user"></i>';

        const content = document.createElement('div');
        content.className = 'message-content';

        const bubble = document.createElement('div');
        bubble.className = 'message-bubble';
        bubble.innerHTML = text.replace(/\n/g, '<br>');

        const time = document.createElement('div');
        time.className = 'message-time';
        time.textContent = getCurrentTime();

        content.appendChild(bubble);
        content.appendChild(time);

        messageDiv.appendChild(avatar);
        messageDiv.appendChild(content);

        messagesContainer.appendChild(messageDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    function updateSuggestions(actions) {
        suggestionsContainer.innerHTML = '';
        actions.forEach(action => {
            const btn = document.createElement('button');
            btn.className = 'suggestion-btn';
            btn.setAttribute('data-message', action);
            btn.textContent = action;
            btn.addEventListener('click', function() {
                messageInput.value = action;
                sendMessage();
            });
            suggestionsContainer.appendChild(btn);
        });
    }

    function generateSessionId() {
        return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    }

    function getUserId() {
        // TODO: Get actual user ID from session/JWT
        return null;
    }

    function getCurrentTime() {
        const now = new Date();
        return now.getHours().toString().padStart(2, '0') + ':' +
               now.getMinutes().toString().padStart(2, '0');
    }
});
</script>
