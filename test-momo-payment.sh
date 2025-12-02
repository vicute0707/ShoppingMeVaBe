#!/bin/bash

# Script test MoMo Payment
# Cửa Hàng Mẹ và Bé

echo "========================================="
echo "  Test MoMo Payment Integration"
echo "========================================="
echo ""

# Kiểm tra ứng dụng có đang chạy không
if ! curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
    echo "❌ Ứng dụng chưa chạy!"
    echo "Vui lòng chạy: ./start-with-ngrok.sh"
    exit 1
fi

echo "✅ Ứng dụng đang chạy"
echo ""

# Lấy ngrok URL
NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o '"public_url":"https://[^"]*' | grep -o 'https://[^"]*' | head -1)

if [ -z "$NGROK_URL" ]; then
    echo "❌ Ngrok chưa chạy hoặc không thể lấy URL"
    exit 1
fi

echo "✅ Ngrok URL: $NGROK_URL"
echo ""

# Kiểm tra MoMo configuration
echo "📋 MoMo Configuration:"
echo "   Endpoint:    https://test-payment.momo.vn/v2/gateway/api/create"
echo "   Partner:     MOMO"
echo "   Callback:    $NGROK_URL/payment/momo/callback"
echo "   IPN:         $NGROK_URL/payment/momo/ipn"
echo ""

# Test các endpoint
echo "🧪 Testing Endpoints..."
echo ""

# Test home
echo "1. Testing Home Page..."
HOME_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/)
if [ "$HOME_STATUS" == "200" ]; then
    echo "   ✅ Home: $HOME_STATUS OK"
else
    echo "   ❌ Home: $HOME_STATUS Failed"
fi

# Test login page
echo "2. Testing Login Page..."
LOGIN_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/auth/login)
if [ "$LOGIN_STATUS" == "200" ]; then
    echo "   ✅ Login: $LOGIN_STATUS OK"
else
    echo "   ❌ Login: $LOGIN_STATUS Failed"
fi

# Test products
echo "3. Testing Products Page..."
PRODUCTS_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/products)
if [ "$PRODUCTS_STATUS" == "200" ]; then
    echo "   ✅ Products: $PRODUCTS_STATUS OK"
else
    echo "   ❌ Products: $PRODUCTS_STATUS Failed"
fi

echo ""
echo "========================================="
echo "  Manual Testing Steps"
echo "========================================="
echo ""
echo "Bước 1: Đăng nhập"
echo "   URL: http://localhost:8081/auth/login"
echo "   Test account: admin@shopmevabe.com / admin123"
echo ""
echo "Bước 2: Tạo đơn hàng"
echo "   1. Thêm sản phẩm vào giỏ"
echo "   2. Checkout với COD"
echo "   3. Xác nhận đơn hàng"
echo ""
echo "Bước 3: Thanh toán MoMo"
echo "   1. Vào 'Đơn hàng của tôi'"
echo "   2. Click vào đơn hàng"
echo "   3. Click 'Thanh toán MoMo'"
echo ""
echo "Bước 4: Test MoMo"
echo "   Card: 9704 0000 0000 0018"
echo "   Name: NGUYEN VAN A"
echo "   Date: 03/07"
echo "   OTP: OTP"
echo ""
echo "========================================="
echo "  URLs"
echo "========================================="
echo "🌐 Application:    http://localhost:8081"
echo "🌐 Public URL:     $NGROK_URL"
echo "📊 Ngrok Dashboard: http://localhost:4040"
echo "📝 Swagger API:    http://localhost:8081/swagger-ui.html"
echo ""
echo "✅ Ready for testing!"
echo ""
