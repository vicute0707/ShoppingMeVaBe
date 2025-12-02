# Hướng Dẫn Deploy Backend lên AWS EC2

## Mục lục
1. [Tạo EC2 Instance](#1-tạo-ec2-instance)
2. [Cấu hình Security Group](#2-cấu-hình-security-group)
3. [Kết nối SSH và cài đặt](#3-kết-nối-ssh-và-cài-đặt)
4. [Deploy Spring Boot Application](#4-deploy-spring-boot-application)
5. [Setup PM2 hoặc systemd](#5-setup-pm2-hoặc-systemd)
6. [Cấu hình Nginx Reverse Proxy](#6-cấu-hình-nginx-reverse-proxy)

---

## 1. Tạo EC2 Instance

### Bước 1.1: Đăng nhập AWS Console
1. Truy cập: https://console.aws.amazon.com/
2. Đăng nhập với tài khoản AWS của bạn
3. Chọn Region: **Asia Pacific (Singapore) - ap-southeast-1** (hoặc region gần bạn nhất)

### Bước 1.2: Launch EC2 Instance
1. **Tìm EC2 Service**:
   - Search bar → gõ "EC2" → Click "EC2"

2. **Launch Instance**:
   - Click nút "Launch instance" (màu cam)

3. **Cấu hình Instance**:

#### A. Name and tags
```
Name: ShopMeVaBe-Production
Environment: production
Application: shop-me-va-be
```

#### B. Application and OS Images (AMI)
```
Amazon Machine Image (AMI):
- Ubuntu Server 22.04 LTS (HVM), SSD Volume Type
- Architecture: 64-bit (x86)
- FREE TIER ELIGIBLE ✅
```

#### C. Instance type
```
t2.micro (Free tier eligible) - CHO TESTING
hoặc
t2.small (Recommended cho production nhẹ)
- 1 vCPU
- 2 GiB Memory
- $0.023/hour (~$16.7/tháng)

hoặc
t3.small (Tốt hơn cho production)
- 2 vCPU
- 2 GiB Memory
- $0.021/hour (~$15.3/tháng)
```

#### D. Key pair (login)
```
Click "Create new key pair"
---
Key pair name: shop-me-va-be-key
Key pair type: RSA
Private key file format: .pem (cho macOS/Linux) hoặc .ppk (cho Windows/PuTTY)
---
Click "Create key pair"
```

**LƯU Ý**: File `.pem` sẽ tự động download. **Lưu file này cẩn thận**, không mất!

#### E. Network settings
```
✅ Allow SSH traffic from: My IP
✅ Allow HTTPS traffic from the internet
✅ Allow HTTP traffic from the internet
```

#### F. Configure storage
```
Root volume:
- Size (GiB): 20 GB (tối thiểu)
- Volume type: gp3 (General Purpose SSD)
- Delete on termination: ✅
- Encrypted: ✅ (recommended)
```

4. **Launch Instance**:
   - Click "Launch instance" (nút màu cam bên phải)
   - Chờ 1-2 phút để instance khởi động

---

## 2. Cấu hình Security Group

### Bước 2.1: Tìm Security Group
1. EC2 Dashboard → Left menu → "Security Groups"
2. Tìm security group của instance vừa tạo (tên thường bắt đầu với `sg-`)

### Bước 2.2: Edit Inbound Rules
Click vào Security Group → Tab "Inbound rules" → "Edit inbound rules"

**Thêm các rules sau**:

| Type | Protocol | Port Range | Source | Description |
|------|----------|------------|--------|-------------|
| SSH | TCP | 22 | My IP | SSH access |
| HTTP | TCP | 80 | 0.0.0.0/0, ::/0 | HTTP public |
| HTTPS | TCP | 443 | 0.0.0.0/0, ::/0 | HTTPS public |
| Custom TCP | TCP | 8081 | 0.0.0.0/0 | Spring Boot App |
| Custom TCP | TCP | 3306 | sg-xxxxx (same SG) | MySQL/MariaDB |

**LƯU Ý**:
- `0.0.0.0/0` = cho phép từ mọi IP (public)
- `My IP` = chỉ cho phép từ IP hiện tại của bạn
- `::/0` = IPv6 version của `0.0.0.0/0`

Click "Save rules"

---

## 3. Kết nối SSH và Cài đặt

### Bước 3.1: Lấy Public IP của EC2
1. EC2 Dashboard → "Instances"
2. Click vào instance đã tạo
3. Copy **Public IPv4 address** (ví dụ: `54.169.123.45`)

### Bước 3.2: Kết nối SSH

#### Trên macOS/Linux:
```bash
# Chmod key file (chỉ cần làm 1 lần)
chmod 400 ~/Downloads/shop-me-va-be-key.pem

# SSH vào EC2
ssh -i ~/Downloads/shop-me-va-be-key.pem ubuntu@54.169.123.45
```

#### Trên Windows (dùng PuTTY):
1. Download PuTTY: https://www.putty.org/
2. Convert .pem sang .ppk bằng PuTTYgen
3. Mở PuTTY:
   - Host Name: `ubuntu@54.169.123.45`
   - Port: 22
   - Connection → SSH → Auth → Browse → chọn file `.ppk`
   - Click "Open"

### Bước 3.3: Update System
```bash
# Update packages
sudo apt update && sudo apt upgrade -y

# Install essentials
sudo apt install -y curl wget git vim htop
```

### Bước 3.4: Install Java 17
```bash
# Install OpenJDK 17
sudo apt install -y openjdk-17-jdk

# Verify
java -version
# Output: openjdk version "17.0.x"
```

### Bước 3.5: Install MariaDB
```bash
# Install MariaDB
sudo apt install -y mariadb-server

# Start MariaDB
sudo systemctl start mariadb
sudo systemctl enable mariadb

# Secure installation
sudo mysql_secure_installation
# Nhấn Enter cho root password (không set)
# Set root password? Y → nhập password mới (ví dụ: YourStrongPassword123!)
# Remove anonymous users? Y
# Disallow root login remotely? Y
# Remove test database? Y
# Reload privilege tables? Y

# Create database
sudo mysql -u root -p
```

Trong MySQL console:
```sql
CREATE DATABASE shop_me_va_be CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'shopmevabe'@'localhost' IDENTIFIED BY 'YourDBPassword123!';

GRANT ALL PRIVILEGES ON shop_me_va_be.* TO 'shopmevabe'@'localhost';

FLUSH PRIVILEGES;

EXIT;
```

### Bước 3.6: Install Maven
```bash
# Install Maven
sudo apt install -y maven

# Verify
mvn -version
```

---

## 4. Deploy Spring Boot Application

### Bước 4.1: Clone Repository
```bash
# Tạo thư mục
mkdir -p ~/apps
cd ~/apps

# Clone từ GitHub
git clone https://github.com/vicute0707/ShoppingMeVaBe.git
cd ShoppingMeVaBe
```

### Bước 4.2: Cấu hình Application Properties
```bash
# Edit application.properties
nano src/main/resources/application.properties
```

**Cập nhật**:
```properties
# Database
spring.datasource.url=jdbc:mariadb://localhost:3306/shop_me_va_be?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh
spring.datasource.username=shopmevabe
spring.datasource.password=YourDBPassword123!

# Server
server.port=8081
app.base-url=https://shopmevabe.landinghub.shop

# MoMo callback URLs
momo.redirect-url=${app.base-url}/payment/momo/callback
momo.ipn-url=${app.base-url}/payment/momo/ipn

# Gemini AI
gemini.api.key=YOUR_ACTUAL_GEMINI_API_KEY
gemini.model=gemini-2.0-flash-exp

# Email
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password

# Cloudinary
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```

**Lấy Gemini API Key**:
1. Truy cập: https://aistudio.google.com/app/apikey
2. Đăng nhập Google
3. Click "Create API key"
4. Copy key và paste vào `gemini.api.key`

Save file: `Ctrl+X` → `Y` → `Enter`

### Bước 4.3: Import Database
```bash
# Import schema và data
mysql -u shopmevabe -p shop_me_va_be < src/main/resources/db/schema.sql
mysql -u shopmevabe -p shop_me_va_be < src/main/resources/db/data.sql
# Nhập password: YourDBPassword123!
```

### Bước 4.4: Build Application
```bash
# Clean và build
./mvnw clean package -DskipTests

# Kiểm tra file JAR
ls -lh target/*.jar
# Nên thấy file: target/www-0.0.1-SNAPSHOT.jar
```

### Bước 4.5: Test Run Application
```bash
# Run thử
java -jar target/www-0.0.1-SNAPSHOT.jar

# Kiểm tra log
# Nếu thấy: "Started ShoppingMomadnBabyApplication" → SUCCESS!

# Stop: Ctrl+C
```

---

## 5. Setup systemd Service

### Bước 5.1: Tạo Service File
```bash
sudo nano /etc/systemd/system/shop-me-va-be.service
```

**Nội dung**:
```ini
[Unit]
Description=Shop Me Va Be Spring Boot Application
After=syslog.target network.target mariadb.service

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu/apps/ShoppingMeVaBe
ExecStart=/usr/bin/java -jar /home/ubuntu/apps/ShoppingMeVaBe/target/www-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

# Logging
StandardOutput=journal
StandardError=journal
SyslogIdentifier=shop-me-va-be

# Environment
Environment="SPRING_PROFILES_ACTIVE=production"

[Install]
WantedBy=multi-user.target
```

Save: `Ctrl+X` → `Y` → `Enter`

### Bước 5.2: Enable và Start Service
```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service (auto-start on boot)
sudo systemctl enable shop-me-va-be

# Start service
sudo systemctl start shop-me-va-be

# Check status
sudo systemctl status shop-me-va-be

# View logs
sudo journalctl -u shop-me-va-be -f
```

**Các lệnh hữu ích**:
```bash
# Stop service
sudo systemctl stop shop-me-va-be

# Restart service
sudo systemctl restart shop-me-va-be

# View full logs
sudo journalctl -u shop-me-va-be --no-pager

# View error logs
sudo journalctl -u shop-me-va-be -p err
```

---

## 6. Cấu hình Nginx Reverse Proxy

### Bước 6.1: Install Nginx
```bash
sudo apt install -y nginx

# Start Nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

### Bước 6.2: Cấu hình Nginx
```bash
sudo nano /etc/nginx/sites-available/shop-me-va-be
```

**Nội dung**:
```nginx
server {
    listen 80;
    server_name shopmevabe.landinghub.shop;

    # Redirect HTTP to HTTPS (sau khi có SSL)
    # return 301 https://$server_name$request_uri;

    # Proxy to Spring Boot
    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket support
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";

        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Health check
    location /actuator/health {
        proxy_pass http://localhost:8081/actuator/health;
        access_log off;
    }

    # Static files (nếu có)
    location /uploads/ {
        alias /home/ubuntu/apps/ShoppingMeVaBe/src/main/resources/static/uploads/;
    }

    # Logs
    access_log /var/log/nginx/shop-me-va-be-access.log;
    error_log /var/log/nginx/shop-me-va-be-error.log;
}
```

Save file.

### Bước 6.3: Enable Site
```bash
# Create symlink
sudo ln -s /etc/nginx/sites-available/shop-me-va-be /etc/nginx/sites-enabled/

# Test configuration
sudo nginx -t

# Reload Nginx
sudo systemctl reload nginx
```

### Bước 6.4: Install Let's Encrypt SSL (Optional - sau khi setup DNS)
```bash
# Install Certbot
sudo apt install -y certbot python3-certbot-nginx

# Get SSL certificate (SAU KHI ĐÃ SETUP DNS)
sudo certbot --nginx -d shopmevabe.landinghub.shop

# Auto-renewal
sudo certbot renew --dry-run
```

---

## 7. Kiểm tra Deployment

### Test từ EC2:
```bash
# Test local
curl http://localhost:8081/actuator/health

# Should return: {"status":"UP"}
```

### Test từ bên ngoài:
1. Lấy Public IP của EC2: `54.169.123.45`
2. Truy cập: `http://54.169.123.45`
3. Hoặc: `http://54.169.123.45:8081`

**Expected**: Thấy trang home của Shop Me Va Be

---

## 8. Update Code (Sau này)

Khi có code mới:
```bash
cd ~/apps/ShoppingMeVaBe

# Pull latest code
git pull origin main

# Rebuild
./mvnw clean package -DskipTests

# Restart service
sudo systemctl restart shop-me-va-be

# Check status
sudo systemctl status shop-me-va-be

# View logs
sudo journalctl -u shop-me-va-be -f
```

---

## 9. Monitoring & Troubleshooting

### View Application Logs:
```bash
# Real-time logs
sudo journalctl -u shop-me-va-be -f

# Last 100 lines
sudo journalctl -u shop-me-va-be -n 100

# Errors only
sudo journalctl -u shop-me-va-be -p err
```

### View Nginx Logs:
```bash
# Access log
sudo tail -f /var/log/nginx/shop-me-va-be-access.log

# Error log
sudo tail -f /var/log/nginx/shop-me-va-be-error.log
```

### Check Disk Space:
```bash
df -h
```

### Check Memory:
```bash
free -h
htop
```

### Check Ports:
```bash
sudo netstat -tulpn | grep -E ':(80|443|8081|3306)'
```

---

## 10. Security Best Practices

1. **Firewall (UFW)**:
```bash
# Enable UFW
sudo ufw enable

# Allow SSH
sudo ufw allow 22/tcp

# Allow HTTP/HTTPS
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Allow Spring Boot (nếu cần test direct)
sudo ufw allow 8081/tcp

# Status
sudo ufw status
```

2. **Fail2Ban** (chống brute force):
```bash
sudo apt install -y fail2ban
sudo systemctl enable fail2ban
sudo systemctl start fail2ban
```

3. **Auto Updates**:
```bash
sudo apt install -y unattended-upgrades
sudo dpkg-reconfigure -plow unattended-upgrades
```

---

## Tổng kết

✅ EC2 Instance đã chạy
✅ Java 17 installed
✅ MariaDB configured
✅ Spring Boot deployed
✅ Nginx reverse proxy
✅ systemd service enabled
✅ Auto-start on boot

**Next Step**: Cấu hình CloudFront + Route 53 (xem file `AWS_DEPLOY_CLOUDFRONT_ROUTE53_GUIDE.md`)

---

**Lưu ý**: Thay thế các giá trị sau cho phù hợp:
- `54.169.123.45` → Public IP của EC2 bạn
- `YourDBPassword123!` → DB password thật
- `YOUR_ACTUAL_GEMINI_API_KEY` → Gemini API key thật
- Email và Cloudinary credentials
