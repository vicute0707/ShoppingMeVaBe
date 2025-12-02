#!/bin/bash

# Script tự động chạy ứng dụng với Ngrok
# Cửa Hàng Mẹ và Bé - MoMo Payment Integration

echo "========================================="
echo "  Shopping Mẹ và Bé - MoMo Payment"
echo "========================================="
echo ""

# Kiểm tra ngrok đã cài chưa
if ! command -v ngrok &> /dev/null
then
    echo "❌ Ngrok chưa được cài đặt!"
    echo ""
    echo "Hướng dẫn cài đặt:"
    echo "  1. Truy cập: https://ngrok.com/download"
    echo "  2. Tải và cài đặt ngrok"
    echo "  3. Đăng ký tài khoản: https://dashboard.ngrok.com/signup"
    echo "  4. Cấu hình authtoken: ngrok config add-authtoken YOUR_TOKEN"
    echo ""
    exit 1
fi

echo "✅ Ngrok đã được cài đặt"
echo ""

# Kiểm tra port 8081 có đang được dùng không
if lsof -Pi :8081 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
    echo "⚠️  Port 8081 đang được sử dụng"
    echo "Đang dừng process..."
    kill $(lsof -t -i:8081) 2>/dev/null
    sleep 2
fi

# Chạy ngrok trong background
echo "🚀 Đang khởi động Ngrok..."
ngrok http 8081 > /dev/null &
NGROK_PID=$!

# Đợi ngrok khởi động
sleep 3

# Lấy public URL từ ngrok
echo "📡 Đang lấy Ngrok URL..."
NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o '"public_url":"https://[^"]*' | grep -o 'https://[^"]*' | head -1)

if [ -z "$NGROK_URL" ]; then
    echo "❌ Không thể lấy Ngrok URL"
    echo "Vui lòng kiểm tra:"
    echo "  1. Ngrok đã được cấu hình authtoken chưa"
    echo "  2. Kết nối internet"
    kill $NGROK_PID 2>/dev/null
    exit 1
fi

echo "✅ Ngrok URL: $NGROK_URL"
echo ""

# Cập nhật application.properties
PROPS_FILE="src/main/resources/application.properties"
echo "📝 Đang cập nhật application.properties..."

# Backup file gốc
cp $PROPS_FILE ${PROPS_FILE}.backup

# Cập nhật app.base-url
sed -i.bak "s|app.base-url=.*|app.base-url=$NGROK_URL|g" $PROPS_FILE

echo "✅ Đã cập nhật: app.base-url=$NGROK_URL"
echo ""

# Hiển thị thông tin
echo "========================================="
echo "  Thông tin hệ thống"
echo "========================================="
echo "🌐 Ngrok URL:     $NGROK_URL"
echo "🔗 Local URL:     http://localhost:8081"
echo "📊 Ngrok Dashboard: http://localhost:4040"
echo "========================================="
echo ""

# Hiển thị các endpoint MoMo
echo "📌 MoMo Endpoints:"
echo "   Callback: $NGROK_URL/payment/momo/callback"
echo "   IPN:      $NGROK_URL/payment/momo/ipn"
echo ""

# Chạy ứng dụng
echo "🚀 Đang khởi động ứng dụng Spring Boot..."
echo "   (Nhấn Ctrl+C để dừng)"
echo ""

# Cleanup function
cleanup() {
    echo ""
    echo "🛑 Đang dừng các service..."

    # Khôi phục file properties
    if [ -f "${PROPS_FILE}.backup" ]; then
        mv ${PROPS_FILE}.backup $PROPS_FILE
        echo "✅ Đã khôi phục application.properties"
    fi

    # Dừng ngrok
    kill $NGROK_PID 2>/dev/null
    echo "✅ Đã dừng Ngrok"

    echo "👋 Tạm biệt!"
    exit 0
}

# Đăng ký cleanup khi thoát
trap cleanup EXIT INT TERM

# Chạy Spring Boot
./mvnw spring-boot:run

# Script sẽ cleanup khi Spring Boot dừng
