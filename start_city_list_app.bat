@echo off

REM Check if Java 17 is installed
java -version 2>nul | findstr /i "17." >nul
if errorlevel 1 (
    echo Java 17 is not installed or is not the default version. Please install Java 17 and try again.
    exit /b 1
)

echo "Found java 17"

REM Check if Angular 16 is installed
ng version 2>nul | findstr /i "16" >nul
if errorlevel 1 (
    echo Angular 16 is not installed. Please install Angular 16 and try again.
    exit /b 1
)

echo "Found Angular 16"

REM Change to the root directory of your Spring Boot project
cd citylist.be

echo "Starting backend ...."

REM Build the Spring Boot app and start the server in the background, redirecting the output to a log file
gradlew build -x test && gradlew bootRun --args='--spring.profiles.active=dev' >> spring-boot-app.log 2>&1 &

REM Wait for the Spring Boot server to start up
timeout /t 15 >nul

REM Change to the root directory of your Angular project
cd ..
cd citylist-ui

REM Install dependencies and start the Angular app in the browser
npm install && ng serve --open
