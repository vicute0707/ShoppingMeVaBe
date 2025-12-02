# Hướng Dẫn Cấu hình CloudFront + Route 53 cho Subdomain

## Thông tin hiện có
```
Base Domain: landinghub.shop
Subdomain: shopmevabe.landinghub.shop
S3 Bucket: landinghub-iconic
CloudFront Distribution ID: E3E6ZTC75HGQKN
CloudFront Domain: d197hx8bwkos4.cloudfront.net
Route 53 Hosted Zone ID: Z05183223V0AYFR0V7OEN
```

## Mục lục
1. [Tạo SSL Certificate (ACM)](#1-tạo-ssl-certificate-acm)
2. [Cấu hình Origin cho EC2](#2-cấu-hình-origin-cho-ec2)
3. [Cập nhật CloudFront Distribution](#3-cập-nhật-cloudfront-distribution)
4. [Cấu hình Route 53 cho Subdomain](#4-cấu-hình-route-53-cho-subdomain)
5. [Testing](#5-testing)

---

## 1. Tạo SSL Certificate (ACM)

### Bước 1.1: Mở AWS Certificate Manager
1. AWS Console → Search "Certificate Manager" hoặc "ACM"
2. **Quan trọng**: Chọn Region **us-east-1 (N. Virginia)**
   - CloudFront CHỈ dùng certificates từ us-east-1

### Bước 1.2: Request Certificate
1. Click "Request a certificate"
2. Chọn "Request a public certificate" → Next

### Bước 1.3: Domain names
```
Fully qualified domain name:
*.landinghub.shop

Add another name to this certificate:
landinghub.shop
shopmevabe.landinghub.shop
```

**Giải thích**:
- `*.landinghub.shop` = wildcard cho tất cả subdomain
- `landinghub.shop` = apex domain
- `shopmevabe.landinghub.shop` = subdomain cụ thể

### Bước 1.4: Validation method
```
Chọn: DNS validation - recommended
```

### Bước 1.5: Key algorithm
```
Chọn: RSA 2048
```

Click "Request"

### Bước 1.6: DNS Validation
1. Sau khi request, bạn sẽ thấy màn hình "Certificate status: Pending validation"
2. Expand domain names → Click "Create records in Route 53"
3. AWS sẽ tự động tạo CNAME records trong Route 53
4. Click "Create records"
5. Chờ 5-10 phút để validation hoàn tất
6. Refresh page → Status sẽ chuyển sang **Issued**

**Lưu ý**: Nếu "Create records in Route 53" không hoạt động:
1. Copy CNAME name và CNAME value
2. Vào Route 53 → Hosted zones → landinghub.shop
3. Create record → Type: CNAME → Paste values
4. Save

---

## 2. Cấu hình Origin cho EC2

### Option A: CloudFront pointing trực tiếp đến EC2

#### Bước 2.1: Lấy Public DNS của EC2
1. EC2 Console → Instances
2. Chọn instance → Copy **Public IPv4 DNS**
   - Ví dụ: `ec2-54-169-123-45.ap-southeast-1.compute.amazonaws.com`

#### Bước 2.2: Elastic IP (Optional nhưng Recommended)
**Tại sao cần**: EC2 public IP thay đổi khi restart

1. EC2 Console → Left menu → "Elastic IPs"
2. Click "Allocate Elastic IP address"
3. Region: Same as EC2
4. Click "Allocate"
5. Select Elastic IP → Actions → "Associate Elastic IP address"
6. Instance: chọn EC2 instance của bạn
7. Click "Associate"

Giờ EC2 của bạn có IP tĩnh!

---

## 3. Cập nhật CloudFront Distribution

### Bước 3.1: Mở CloudFront Console
1. AWS Console → Search "CloudFront"
2. Click vào Distribution ID `E3E6ZTC75HGQKN`

### Bước 3.2: Add Origin cho EC2

1. Tab "Origins" → Click "Create origin"

**Origin settings**:
```
Origin domain: ec2-54-169-123-45.ap-southeast-1.compute.amazonaws.com
(hoặc Elastic IP nếu có)

Protocol: HTTP only (hoặc HTTPS nếu EC2 có SSL)

HTTP port: 80
HTTPS port: 443

Origin path: (để trống)

Name: ShopMeVaBe-EC2-Origin

Enable Origin Shield: No

Additional settings:
- Minimum origin SSL protocol: TLSv1.2
- Origin response timeout: 30 seconds
- Origin keep-alive timeout: 5 seconds
```

Click "Create origin"

### Bước 3.3: Create Behavior cho API requests

1. Tab "Behaviors" → Click "Create behavior"

**Behavior settings**:
```
Path pattern: /api/*

Origin and origin groups: ShopMeVaBe-EC2-Origin

Viewer protocol policy: Redirect HTTP to HTTPS

Allowed HTTP methods: GET, HEAD, OPTIONS, PUT, POST, PATCH, DELETE

Cache key and origin requests:
- Cache policy: CachingDisabled (cho API dynamic content)
- Origin request policy: AllViewer

Response headers policy: SimpleCORS (nếu cần CORS)

Function associations: None
```

Click "Create behavior"

### Bước 3.4: Create Behavior cho static content (từ S3)

1. Tab "Behaviors" → Click "Create behavior"

**Behavior settings**:
```
Path pattern: /static/*

Origin: landinghub-iconic.s3.amazonaws.com (origin S3 hiện có)

Viewer protocol policy: Redirect HTTP to HTTPS

Allowed HTTP methods: GET, HEAD

Cache policy: CachingOptimized

Origin request policy: CORS-S3Origin
```

Click "Create behavior"

### Bước 3.5: Create Behavior cho uploads (images)

```
Path pattern: /uploads/*

Origin: ShopMeVaBe-EC2-Origin

Cache policy: CachingOptimized (cache images)

Origin request policy: AllViewer
```

### Bước 3.6: Update Default Behavior

1. Tab "Behaviors" → Select "Default (*)" → Edit

```
Origin: ShopMeVaBe-EC2-Origin

Viewer protocol policy: Redirect HTTP to HTTPS

Allowed HTTP methods: GET, HEAD, OPTIONS, PUT, POST, PATCH, DELETE

Cache policy: CachingDisabled (cho dynamic web pages)

Origin request policy: AllViewer

Compress objects automatically: Yes
```

Save changes

### Bước 3.7: Update Distribution Settings

1. Tab "General" → Click "Edit"

**Settings**:
```
Alternate domain names (CNAMEs):
shopmevabe.landinghub.shop

Custom SSL certificate:
[Select certificate created in Step 1]

Security policy: TLSv1.2_2021 (recommended)

Supported HTTP versions: HTTP/2, HTTP/1.1

Standard logging: On (optional)
- S3 bucket: landinghub-iconic
- Log prefix: cloudfront-logs/

IPv6: On
```

Click "Save changes"

**Lưu ý**: Distribution sẽ mất 10-15 phút để deploy changes.

Check status: "Last modified" column → chờ "Deployed"

---

## 4. Cấu hình Route 53 cho Subdomain

### Bước 4.1: Mở Route 53 Console
1. AWS Console → Search "Route 53"
2. Left menu → "Hosted zones"
3. Click vào `landinghub.shop` (Hosted Zone ID: Z05183223V0AYFR0V7OEN)

### Bước 4.2: Create A Record cho Subdomain

1. Click "Create record"

**Record settings**:
```
Record name: shopmevabe
(sẽ tạo: shopmevabe.landinghub.shop)

Record type: A – Routes traffic to an IPv4 address and some AWS resources

Alias: Yes (toggle ON)

Route traffic to:
- Alias to CloudFront distribution
- Region: (auto-select)
- CloudFront distribution: d197hx8bwkos4.cloudfront.net

Routing policy: Simple routing

Evaluate target health: No
```

Click "Create records"

### Bước 4.3: Create AAAA Record (IPv6) - Optional

Làm tương tự như A record nhưng:
```
Record type: AAAA – Routes traffic to an IPv6 address and some AWS resources

(Các settings khác giống hệt A record)
```

---

## 5. Testing

### Bước 5.1: DNS Propagation Check
```bash
# Check từ terminal
dig shopmevabe.landinghub.shop

# hoặc
nslookup shopmevabe.landinghub.shop
```

Hoặc online tool:
- https://dnschecker.org/
- Nhập: `shopmevabe.landinghub.shop`
- Check từ multiple locations

### Bước 5.2: Test HTTP/HTTPS
```bash
# Test HTTP (sẽ redirect sang HTTPS)
curl -I http://shopmevabe.landinghub.shop

# Test HTTPS
curl -I https://shopmevabe.landinghub.shop

# Test API endpoint
curl https://shopmevabe.landinghub.shop/api/chatbot/health
```

### Bước 5.3: Test từ Browser

1. **Home page**:
   - https://shopmevabe.landinghub.shop/

2. **API endpoint**:
   - https://shopmevabe.landinghub.shop/api/chatbot/health
   - https://shopmevabe.landinghub.shop/api/products

3. **Static files** (nếu có):
   - https://shopmevabe.landinghub.shop/static/...

4. **Actuator** (health check):
   - https://shopmevabe.landinghub.shop/actuator/health

### Bước 5.4: Test AI Chatbot

1. Truy cập: https://shopmevabe.landinghub.shop/
2. Click vào chatbot icon ở góc phải màn hình
3. Gửi tin nhắn: "Xin chào"
4. Kiểm tra AI response

### Bước 5.5: Test MoMo Payment

1. Đăng nhập vào website
2. Tạo đơn hàng
3. Thanh toán MoMo
4. Kiểm tra callback có hoạt động không

---

## 6. Cấu hình Bổ sung

### 6.1: CloudFront Cache Invalidation

Khi deploy code mới, clear CloudFront cache:

1. CloudFront Console → Distribution → Tab "Invalidations"
2. Click "Create invalidation"
3. Object paths:
```
/*
/api/*
/static/*
```
4. Click "Create invalidation"

**Hoặc dùng script**:
```bash
# Install AWS CLI
aws configure

# Invalidate cache
aws cloudfront create-invalidation \
  --distribution-id E3E6ZTC75HGQKN \
  --paths "/*"
```

### 6.2: Custom Error Pages

CloudFront → Distribution → Tab "Error pages" → Create custom error response

```
HTTP error code: 404
Customize error response: Yes
Response page path: /error.html
HTTP response code: 404
Error caching minimum TTL: 300
```

Làm tương tự cho 403, 500, 503

### 6.3: Enable Logging

CloudFront → Distribution → Edit → Settings:
```
Standard logging: On
S3 bucket for logs: landinghub-iconic
Log prefix: cloudfront-logs/shopmevabe/
Cookie logging: On
```

---

## 7. Monitoring & Troubleshooting

### 7.1: CloudFront Metrics

CloudFront Console → Distribution → Tab "Monitoring"

Metrics:
- Requests
- Data transferred
- Error rate
- Cache hit rate

### 7.2: CloudWatch Alarms

1. CloudWatch Console → Alarms → Create alarm
2. Select CloudFront metrics
3. Set threshold (ví dụ: 5xx errors > 10)
4. Set SNS notification

### 7.3: CloudFront Access Logs

1. S3 Console → landinghub-iconic bucket
2. Navigate to `cloudfront-logs/shopmevabe/`
3. Download `.gz` files
4. Analyze logs

### 7.4: Common Issues

#### Issue: "403 Forbidden"
**Nguyên nhân**:
- Origin không accessible từ CloudFront
- Security Group của EC2 không allow CloudFront IPs

**Fix**:
```bash
# EC2 Security Group → Inbound rules
# Add rule:
Type: HTTP
Port: 80
Source: 0.0.0.0/0 (hoặc CloudFront IPs từ: https://d7uri8nf7uskq.cloudfront.net/tools/list-cloudfront-ips)
```

#### Issue: "504 Gateway Timeout"
**Nguyên nhân**:
- EC2 app không running
- Port 8081 không open

**Fix**:
```bash
# Check service
sudo systemctl status shop-me-va-be

# Check port
sudo netstat -tulpn | grep 8081

# Restart service
sudo systemctl restart shop-me-va-be
```

#### Issue: "DNS không resolve"
**Nguyên nhân**:
- Route 53 record chưa propagate
- CNAME/A record sai

**Fix**:
```bash
# Check DNS
dig shopmevabe.landinghub.shop

# Wait 5-10 minutes for propagation
```

#### Issue: "SSL Certificate Invalid"
**Nguyên nhân**:
- Certificate chưa được validate
- Certificate không ở region us-east-1

**Fix**:
1. ACM Console → Check certificate status = "Issued"
2. Ensure region = us-east-1
3. Re-create certificate nếu cần

---

## 8. Cost Optimization

### CloudFront Pricing

**Free Tier** (12 tháng đầu):
- 50 GB data transfer out
- 2,000,000 HTTP/HTTPS requests

**Sau free tier**:
- Data transfer: $0.085/GB (Asia)
- HTTP requests: $0.0090 per 10,000
- HTTPS requests: $0.0120 per 10,000

**Ước tính** (cho 10,000 users/month):
- ~100 GB transfer: $8.5
- ~1M requests: $1.2
- **Total**: ~$10/month

### S3 Storage Pricing
- First 50 TB: $0.023/GB/month
- Ước tính 10 GB: $0.23/month

### EC2 Pricing
- t2.micro (free tier): $0
- t2.small: ~$17/month
- t3.small: ~$15/month

**Total Monthly Cost** (production):
- EC2: $15-17
- CloudFront: $10
- S3: $0.50
- Route 53: $0.50
- **Grand Total**: ~$26-28/month

---

## 9. Architecture Diagram

```
Internet
    │
    ├─ Route 53 DNS (shopmevabe.landinghub.shop)
    │
    └─► CloudFront Distribution (d197hx8bwkos4.cloudfront.net)
         │
         ├─ Origin 1: EC2 (for dynamic content /api/*, pages)
         │   │
         │   └─► Nginx → Spring Boot App :8081
         │       │
         │       └─► MariaDB
         │
         └─ Origin 2: S3 (for static content /static/*)
             └─► landinghub-iconic bucket
```

---

## 10. Tổng kết

✅ SSL Certificate created & validated
✅ CloudFront configured với multi-origins
✅ Route 53 A record created
✅ HTTPS enabled
✅ Domain `shopmevabe.landinghub.shop` hoạt động
✅ API, static files, và pages đều qua CloudFront CDN
✅ Chatbot AI ready to use

**Next Steps**:
1. Test toàn bộ chức năng trên production domain
2. Monitor CloudFront metrics
3. Optimize cache settings
4. Setup CloudWatch alarms
5. Regular invalidations sau khi deploy

---

**Support Links**:
- CloudFront Docs: https://docs.aws.amazon.com/cloudfront/
- Route 53 Docs: https://docs.aws.amazon.com/route53/
- ACM Docs: https://docs.aws.amazon.com/acm/

**Lưu ý**: Tất cả changes ở CloudFront mất 10-15 phút để deploy globally!
