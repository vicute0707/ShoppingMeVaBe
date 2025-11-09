#!/bin/bash
# Script chạy ứng dụng trong môi trường Development với H2 Database

echo "🚀 Starting Shop Mẹ và Bé - Development Mode (H2 Database)"
echo "=================================================="
echo ""

# Tạo thư mục data nếu chưa có
mkdir -p ./data

# Xác định Maven command
if [ -x "./mvnw" ]; then
    MVN="./mvnw"
else
    echo "⚠️  Maven wrapper không khả dụng, sử dụng mvn command..."
    MVN="mvn"
fi

# Clean và build project
echo "📦 Building project..."
$MVN clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build thành công!"
    echo ""
    echo "🎯 Khởi động ứng dụng..."
    echo "   - Web: http://localhost:8080"
    echo "   - H2 Console: http://localhost:8080/h2-console"
    echo "   - JDBC URL: jdbc:h2:file:./data/ShopBabyandMomCute"
    echo "   - Username: sa"
    echo "   - Password: (để trống)"
    echo ""

    # Chạy ứng dụng với profile dev
    $MVN spring-boot:run -Dspring-boot.run.profiles=dev
else
    echo ""
    echo "❌ Build thất bại! Vui lòng kiểm tra lỗi ở trên."
    exit 1
fi
