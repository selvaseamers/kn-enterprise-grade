@echo off

REM Set the port numbers for the Spring Boot app and the Angular app
set SPRING_BOOT_PORT=8080
set ANGULAR_PORT=4200


REM Kill the running processes on the specified ports
echo Killing processes on ports %SPRING_BOOT_PORT% and %ANGULAR_PORT%...
for /f "tokens=2 delims=," %%a in ('netstat -aon ^| findstr /r /c:"%SPRING_BOOT_PORT%" /c:"%ANGULAR_PORT%"') do taskkill /f /pid %%a >nul
