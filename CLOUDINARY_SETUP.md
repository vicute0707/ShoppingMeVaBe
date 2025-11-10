# Hướng dẫn cấu hình Cloudinary cho Upload Ảnh

## 🎯 Tại sao sử dụng Cloudinary?

- ✅ **Miễn phí** 25GB storage và 25GB bandwidth/tháng
- ✅ **Tự động tối ưu** ảnh (compression, format conversion)
- ✅ **CDN toàn cầu** - load ảnh nhanh hơn
- ✅ **Không lo storage** - không cần lưu ảnh trên server
- ✅ **Scalable** - phù hợp khi app phát triển

## 📝 Các bước cấu hình

### 1. Đăng ký tài khoản Cloudinary (Free)

1. Truy cập: https://cloudinary.com/users/register/free
2. Điền thông tin đăng ký
3. Xác nhận email

### 2. Lấy thông tin credentials

Sau khi đăng nhập, vào Dashboard (https://cloudinary.com/console):

Bạn sẽ thấy 3 thông tin quan trọng:
- **Cloud Name**: `your-cloud-name` (ví dụ: `dxyzabc123`)
- **API Key**: `123456789012345` (số dài)
- **API Secret**: `aBcDeFgH...` (chuỗi ký tự)

### 3. Cập nhật file `application.properties`

Mở file `src/main/resources/application.properties` và cập nhật:

```properties
# ==========================================
# CLOUDINARY IMAGE UPLOAD
# ==========================================
# Bật Cloudinary (đổi từ false → true)
cloudinary.enabled=true

# Điền thông tin từ Cloudinary Dashboard
cloudinary.cloud-name=your-cloud-name-here
cloudinary.api-key=your-api-key-here
cloudinary.api-secret=your-api-secret-here
```

**Ví dụ thực tế:**
```properties
cloudinary.enabled=true
cloudinary.cloud-name=dxyzabc123
cloudinary.api-key=123456789012345
cloudinary.api-secret=aBcDeFgHiJkLmNoPqRsTuVwXyZ
```

### 4. Build lại project

```bash
mvn clean install
```

### 5. Restart application

```bash
mvn spring-boot:run
```

## ✅ Kiểm tra hoạt động

1. Vào trang **Admin → Quản lý sản phẩm → Thêm sản phẩm**
2. Upload một ảnh
3. Nếu thành công, trong log sẽ thấy:
   ```
   Saved image to Cloudinary: https://res.cloudinary.com/...
   ```
4. Ảnh sẽ được lưu trên Cloudinary, không lưu local nữa!

## 🔄 Chuyển đổi giữa Cloudinary và Local Storage

### Dùng Cloudinary:
```properties
cloudinary.enabled=true
```

### Dùng Local Storage (như cũ):
```properties
cloudinary.enabled=false
```

## 📁 Cấu trúc thư mục trên Cloudinary

Ảnh sẽ được lưu theo cấu trúc:
```
products/
  ├── abc123-def456-ghi789.jpg
  ├── xyz789-uvw456-rst123.png
  └── ...
```

## 🚀 Tính năng tự động

### 1. Fallback tự động
Nếu Cloudinary có lỗi → tự động lưu về local storage

### 2. Xóa ảnh thông minh
Khi xóa/update sản phẩm → tự động xóa ảnh trên Cloudinary

### 3. Tối ưu ảnh tự động
Cloudinary tự động:
- Compress ảnh
- Convert sang format tốt nhất (WebP, AVIF)
- Resize theo yêu cầu

## 🛠 Troubleshooting

### Lỗi: "Failed to upload to Cloudinary"
- ✅ Kiểm tra `cloudinary.enabled=true`
- ✅ Kiểm tra Cloud Name, API Key, API Secret đúng chưa
- ✅ Kiểm tra internet connection

### Ảnh vẫn lưu local
- ✅ Restart lại application sau khi đổi config
- ✅ Kiểm tra file `application.properties` đã save chưa

### Xem log để debug
```bash
tail -f logs/spring.log | grep Cloudinary
```

## 📊 Monitor usage

Xem usage tại: https://cloudinary.com/console/usage

- Free plan: 25GB storage, 25GB bandwidth/month
- Nếu vượt → upgrade hoặc optimize ảnh

## 🔐 Bảo mật

**QUAN TRỌNG**: Không commit credentials lên Git!

Thêm vào `.gitignore`:
```
application.properties
application-*.properties
```

Hoặc dùng environment variables:
```bash
export CLOUDINARY_CLOUD_NAME=your-cloud-name
export CLOUDINARY_API_KEY=your-api-key
export CLOUDINARY_API_SECRET=your-api-secret
```

Và trong `application.properties`:
```properties
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}
```

## 🎉 Done!

Giờ ảnh sẽ được lưu trên Cloudinary với CDN toàn cầu, load nhanh hơn nhiều!
