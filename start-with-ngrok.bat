@echo off
REM Script tự động chạy ứng dụng với Ngrok (Windows)
REM Cửa Hàng Mẹ và Bé - MoMo Payment Integration

echo =========================================
echo   Shopping Me va Be - MoMo Payment
echo =========================================
echo.

REM Kiểm tra ngrok
where ngrok >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [31mNgrok chua duoc cai dat![0m
    echo.
    echo Huong dan cai dat:
    echo   1. Truy cap: https://ngrok.com/download
    echo   2. Tai va cai dat ngrok
    echo   3. Dang ky tai khoan: https://dashboard.ngrok.com/signup
    echo   4. Cau hinh authtoken: ngrok config add-authtoken YOUR_TOKEN
    echo.
    pause
    exit /b 1
)

echo [32mNgrok da duoc cai dat[0m
echo.

REM Dừng process đang dùng port 8081
echo Kiem tra port 8081...
netstat -ano | findstr :8081 >nul
if %ERRORLEVEL% EQU 0 (
    echo [33mPort 8081 dang duoc su dung, dang dong...[0m
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do taskkill /F /PID %%a >nul 2>&1
    timeout /t 2 >nul
)

REM Chạy ngrok trong background
echo [36mDang khoi dong Ngrok...[0m
start /B ngrok http 8081

REM Đợi ngrok khởi động
timeout /t 3 >nul

REM Lấy ngrok URL
echo [36mDang lay Ngrok URL...[0m
for /f "tokens=*" %%i in ('curl -s http://localhost:4040/api/tunnels ^| findstr /R "https://.*ngrok"') do set NGROK_LINE=%%i

REM Parse URL (đơn giản hóa - có thể cần PowerShell cho chính xác hơn)
echo %NGROK_LINE% | findstr "public_url" >nul
if %ERRORLEVEL% NEQ 0 (
    echo [31mKhong the lay Ngrok URL[0m
    echo Vui long kiem tra:
    echo   1. Ngrok da duoc cau hinh authtoken chua
    echo   2. Ket noi internet
    pause
    taskkill /F /IM ngrok.exe >nul 2>&1
    exit /b 1
)

REM Sử dụng PowerShell để lấy URL chính xác
for /f "usebackq tokens=*" %%i in (`powershell -Command "(curl -s http://localhost:4040/api/tunnels | ConvertFrom-Json).tunnels[0].public_url"`) do set NGROK_URL=%%i

echo [32mNgrok URL: %NGROK_URL%[0m
echo.

REM Cập nhật application.properties
set PROPS_FILE=src\main\resources\application.properties
echo [36mDang cap nhat application.properties...[0m

REM Backup
copy %PROPS_FILE% %PROPS_FILE%.backup >nul

REM Update using PowerShell (an toàn hơn)
powershell -Command "(Get-Content %PROPS_FILE%) -replace 'app.base-url=.*', 'app.base-url=%NGROK_URL%' | Set-Content %PROPS_FILE%"

echo [32mDa cap nhat: app.base-url=%NGROK_URL%[0m
echo.

REM Hiển thị thông tin
echo =========================================
echo   Thong tin he thong
echo =========================================
echo [36mNgrok URL:     %NGROK_URL%[0m
echo [36mLocal URL:     http://localhost:8081[0m
echo [36mNgrok Dashboard: http://localhost:4040[0m
echo =========================================
echo.

echo [36mMoMo Endpoints:[0m
echo    Callback: %NGROK_URL%/payment/momo/callback
echo    IPN:      %NGROK_URL%/payment/momo/ipn
echo.

REM Chạy ứng dụng
echo [32mDang khoi dong ung dung Spring Boot...[0m
echo    (Nhan Ctrl+C de dung)
echo.

REM Chạy Spring Boot
call mvnw.cmd spring-boot:run

REM Cleanup khi dừng
echo.
echo [33mDang dung cac service...[0m

REM Khôi phục properties
if exist %PROPS_FILE%.backup (
    move /Y %PROPS_FILE%.backup %PROPS_FILE% >nul
    echo [32mDa khoi phuc application.properties[0m
)

REM Dừng ngrok
taskkill /F /IM ngrok.exe >nul 2>&1
echo [32mDa dung Ngrok[0m

echo [32mTam biet![0m
pause
