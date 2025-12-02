# Quick Start: AI Chatbot + AWS Deployment

## 🎯 Tổng quan

Bạn vừa nhận được:
1. ✨ **AI Chatbot** với Gemini 2.0 Flash
2. 🚀 **Hướng dẫn deploy** lên AWS (EC2 + CloudFront + Route 53)
3. 🎨 **UI đẹp** với animations

---

## 🤖 AI Chatbot Features

### Chức năng:
- 💬 Trả lời câu hỏi về đơn hàng, sản phẩm
- 👤 Quản lý thông tin người dùng
- 🛍️ Gợi ý sản phẩm phù hợp
- 📦 Tracking đơn hàng realtime
- 🎨 UI đẹp với animations

### Tech Stack:
- **AI**: Google Gemini 2.0 Flash
- **Backend**: Spring Boot REST API
- **Frontend**: Vanilla JavaScript + CSS animations
- **Security**: Public API (không cần login để chat)

---

## 🚀 Quick Start - Chạy Local

### 1. Lấy Gemini API Key

```bash
# Truy cập:
https://aistudio.google.com/app/apikey

# Đăng nhập Google → Create API key → Copy
```

### 2. Cập nhật Configuration

File: `src/main/resources/application.properties`

```properties
# Paste Gemini API key của bạn
gemini.api.key=AIzaSy...YOUR_KEY_HERE...
gemini.model=gemini-2.0-flash-exp
```

### 3. Build & Run

```bash
# Clean và build
./mvnw clean package -DskipTests

# Run application
./mvnw spring-boot:run

# Hoặc run JAR
java -jar target/www-0.0.1-SNAPSHOT.jar
```

### 4. Test Chatbot

1. Mở browser: http://localhost:8081
2. Click icon chatbot ở góc phải màn hình 💬
3. Chat: "Xin chào"
4. AI sẽ trả lời!

### 5. Test API (Optional)

```bash
curl -X POST http://localhost:8081/api/chatbot/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Đơn hàng của tôi",
    "userId": 1,
    "sessionId": "test123",
    "contextType": "order"
  }'
```

---

## ☁️ Deploy lên AWS

### Option 1: EC2 Only (Đơn giản nhất)

**Thời gian**: ~30 phút

**Follow**: `AWS_DEPLOY_EC2_GUIDE.md`

**Steps**:
1. Tạo EC2 instance (t2.small)
2. Install Java 17 + MariaDB
3. Deploy Spring Boot app
4. Setup Nginx reverse proxy
5. Get SSL with Let's Encrypt

**Cost**: ~$17/tháng

**Access**: `http://your-ec2-ip:8081`

---

### Option 2: EC2 + CloudFront + Route 53 (Production)

**Thời gian**: ~1-2 giờ

**Follow**:
1. `AWS_DEPLOY_EC2_GUIDE.md` (backend)
2. `AWS_DEPLOY_CLOUDFRONT_ROUTE53_GUIDE.md` (CDN + DNS)

**Steps**:
1. Deploy backend lên EC2
2. Tạo SSL certificate (ACM)
3. Setup CloudFront distribution
4. Cấu hình Route 53 subdomain
5. Point subdomain đến CloudFront

**Cost**: ~$26-28/tháng

**Access**: `https://shopmevabe.landinghub.shop`

**Benefits**:
- ✅ CDN global (nhanh hơn)
- ✅ HTTPS tự động
- ✅ Custom domain
- ✅ DDoS protection
- ✅ Auto-scaling ready

---

## 📚 Documentation Structure

```
AWS_DEPLOY_EC2_GUIDE.md
├── 1. Tạo EC2 Instance
├── 2. Cấu hình Security Group
├── 3. Kết nối SSH
├── 4. Deploy Spring Boot
├── 5. Setup systemd
├── 6. Nginx Reverse Proxy
├── 7. Testing
├── 8. Update Code
└── 9. Monitoring

AWS_DEPLOY_CLOUDFRONT_ROUTE53_GUIDE.md
├── 1. SSL Certificate (ACM)
├── 2. Origin cho EC2
├── 3. CloudFront Distribution
├── 4. Route 53 DNS
├── 5. Testing
├── 6. Cache Invalidation
└── 7. Monitoring
```

---

## 🎨 Chatbot UI Features

### Widget Appearance:
- **Button**: Floating bottom-right với pulse animation
- **Window**: 380x600px chat window
- **Colors**: Purple gradient (#667eea → #764ba2)
- **Icon**: Robot emoji 🤖

### Interactions:
- **Typing indicator**: 3 dots animation
- **Auto-scroll**: Scroll to latest message
- **Suggested actions**: Quick reply buttons
- **Message bubbles**: Different colors for user/bot
- **Time stamps**: Display send time

### Mobile Responsive:
- Full-screen trên mobile
- Touch-friendly button size
- Adaptive textarea

---

## 🔧 Customization

### Change AI Model:
```properties
# application.properties
gemini.model=gemini-2.0-flash-exp
# hoặc
gemini.model=gemini-1.5-pro
```

### Change Colors:
File: `src/main/webapp/WEB-INF/views/common/chatbot.jsp`

```css
/* Line ~30 */
background: linear-gradient(135deg, #YOUR_COLOR1 0%, #YOUR_COLOR2 100%);
```

### Change Position:
```css
/* Line ~10 */
#chatbot-widget {
    bottom: 20px;  /* Thay đổi */
    right: 20px;   /* Thay đổi */
}
```

### Add Custom Prompts:
File: `src/main/java/iuh/student/www/service/GeminiAIChatbotService.java`

```java
// Method: buildPrompt()
// Line ~130
// Thay đổi system prompt
```

---

## 🐛 Troubleshooting

### Chatbot không hiển thị?

**Check**:
1. File `chatbot.jsp` có được include trong `footer.jsp`?
2. jQuery đã load chưa?
3. Check browser console (F12) có lỗi không?

**Fix**:
```bash
# Rebuild
./mvnw clean package -DskipTests

# Clear browser cache
Ctrl+Shift+Delete → Clear cache
```

### AI không trả lời?

**Check**:
1. Gemini API key có đúng không?
2. Check logs:
```bash
# Local
./mvnw spring-boot:run

# EC2
sudo journalctl -u shop-me-va-be -f | grep -i gemini
```

3. Test API trực tiếp:
```bash
curl -X POST http://localhost:8081/api/chatbot/health
```

**Common errors**:
```
Error: 403 Forbidden
→ API key sai hoặc quota exceeded

Error: 400 Bad Request
→ Request body sai format

Error: 500 Internal Server Error
→ Check application logs
```

### Deploy lên EC2 fail?

**Check**:
1. Security Group có allow port 8081?
2. Java 17 installed?
3. MariaDB running?
```bash
sudo systemctl status mariadb
sudo systemctl status shop-me-va-be
```

4. Application logs:
```bash
sudo journalctl -u shop-me-va-be -n 100 --no-pager
```

---

## 📊 Cost Calculator

### Local Development:
- **Cost**: $0 (FREE)
- **Requirements**: Java 17, RAM 4GB

### AWS Deployment:

#### EC2 Only:
| Item | Cost/month |
|------|------------|
| t2.small | $17 |
| **Total** | **$17** |

#### Full Stack (Production):
| Item | Cost/month |
|------|------------|
| EC2 t3.small | $15 |
| CloudFront | $10 |
| Route 53 | $0.50 |
| S3 Storage | $0.50 |
| **Total** | **$26** |

**Free Tier** (12 tháng đầu):
- EC2 t2.micro: FREE
- CloudFront: 50GB FREE
- Route 53: $0.50 (not included)
- **Total**: ~$1/month

---

## 🎯 Next Steps

### Sau khi deploy:

1. **Monitor Performance**:
   - CloudWatch metrics
   - Application logs
   - Error tracking

2. **Optimize**:
   - Cache policies
   - Database indexes
   - Image compression

3. **Scale**:
   - Add load balancer
   - Auto-scaling groups
   - RDS instead of local DB

4. **Security**:
   - WAF rules
   - DDoS protection
   - Backup strategy

---

## 📞 Support

### Documentation:
- EC2 Deploy: `AWS_DEPLOY_EC2_GUIDE.md`
- CloudFront: `AWS_DEPLOY_CLOUDFRONT_ROUTE53_GUIDE.md`
- MoMo Payment: `MOMO_QUICK_START.md`
- Auto-login: `MOMO_AUTO_LOGIN_FIX.md`

### External Resources:
- Gemini API: https://ai.google.dev/
- AWS Docs: https://docs.aws.amazon.com/
- Spring Boot: https://spring.io/guides

---

## ✅ Checklist Deploy

### Local Testing:
- [ ] Gemini API key configured
- [ ] Application runs locally
- [ ] Chatbot appears on page
- [ ] AI responds to messages
- [ ] Database connected
- [ ] MoMo payment works (with ngrok)

### AWS EC2:
- [ ] EC2 instance created
- [ ] Security Group configured
- [ ] Java 17 installed
- [ ] MariaDB setup
- [ ] Application deployed
- [ ] systemd service running
- [ ] Nginx configured
- [ ] SSL certificate (optional)

### AWS CloudFront + Route 53:
- [ ] SSL certificate created (ACM)
- [ ] CloudFront distribution configured
- [ ] Origins added (EC2 + S3)
- [ ] Behaviors configured
- [ ] Route 53 record created
- [ ] DNS propagated
- [ ] HTTPS working
- [ ] Chatbot functional on production

---

**Chúc bạn deploy thành công!** 🎉

Nếu gặp vấn đề, check logs và documentation chi tiết!
