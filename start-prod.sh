#!/bin/bash

##########################################################
# 🍼👶 Cửa Hàng Mẹ và Bé - Production Startup Script
##########################################################

echo "🍼👶 =========================================="
echo "    Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute"
echo "    Starting Production Environment..."
echo "=========================================== 🍼👶"
echo ""

# Check if MySQL is running
echo "📊 Checking MySQL connection..."
if ! mysql -u root -e "SELECT 1" &> /dev/null; then
    echo "❌ MySQL is not running or cannot connect!"
    echo "   Please start MySQL first: sudo systemctl start mysql"
    exit 1
fi
echo "✅ MySQL is running"
echo ""

# Check if database exists
echo "📊 Checking database..."
DB_EXISTS=$(mysql -u root -e "SHOW DATABASES LIKE 'shop_me_va_be_cute';" | grep "shop_me_va_be_cute" || true)
if [ -z "$DB_EXISTS" ]; then
    echo "⚠️  Database 'shop_me_va_be_cute' does not exist!"
    echo "   Creating database..."
    mysql -u root -e "CREATE DATABASE shop_me_va_be_cute CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    echo "✅ Database created successfully"
else
    echo "✅ Database exists"
fi
echo ""

# Check if ngrok is required
echo "🌐 Ngrok Configuration"
echo "   For MoMo payment testing, you need to:"
echo "   1. Run: ngrok http 8080"
echo "   2. Copy the Forwarding URL (https://xxx.ngrok.io)"
echo "   3. Update app.base-url in application-prod.properties"
echo ""
read -p "Have you configured ngrok URL? (y/n): " ngrok_configured
if [ "$ngrok_configured" != "y" ]; then
    echo "⚠️  Please configure ngrok before continuing!"
    echo "   See PRODUCTION_SETUP.md for details"
    exit 1
fi
echo ""

# Build application
echo "🔨 Building application..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi
echo "✅ Build successful"
echo ""

# Start application
echo "🚀 Starting application with production profile..."
echo "   Application will be available at:"
echo "   - Local: http://localhost:8080"
echo "   - Ngrok: Check your ngrok URL"
echo ""
echo "📝 Logs will appear below..."
echo "==========================================="
echo ""

java -jar target/www-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
