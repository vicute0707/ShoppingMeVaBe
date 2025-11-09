#!/bin/bash

##########################################################
# 🍼👶 Cửa Hàng Mẹ và Bé - Development Startup Script
##########################################################

echo "🍼👶 =========================================="
echo "    Cửa Hàng Mẹ và Bé - Shop Baby & Mom Cute"
echo "    Starting Development Environment..."
echo "=========================================== 🍼👶"
echo ""

echo "🔨 Building and starting application..."
echo "   Using H2 in-memory database"
echo "   Application will be available at:"
echo "   - http://localhost:8080"
echo "   - H2 Console: http://localhost:8080/h2-console"
echo ""
echo "📝 Logs will appear below..."
echo "==========================================="
echo ""

mvn spring-boot:run
