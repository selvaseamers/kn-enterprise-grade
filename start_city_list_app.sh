#!/bin/sh

# Check if Java 17 is installed
if ! command -v java &> /dev/null || ! java -version 2>&1 | grep -q "17"; then
    echo "Java 17 is not installed or is not the default version. Please install Java 17 and try again."
    exit 1
fi

echo "Found java 17"

# Check if Angular 16 is installed
if ! command -v ng &> /dev/null || ! ng version 2>&1 | grep -q "16"; then
    echo "Angular 16 is not installed. Please install Angular 16 and try again."
    exit 1
fi

echo "Found Angular 16"

# Change to the root directory of Spring Boot project
cd citylist.be

echo "Starting backend ...."

# Build the Spring Boot app and start the server in the background
./gradlew bootRun --args='--spring.profiles.active=dev' >> spring-boot-app.log 2>&1 &

# Wait for the Spring Boot server to start up
sleep 30

echo "Starting frontend...."

# Change to the root directory of your Angular project
cd ..
cd citylist-ui

# Install dependencies and start the Angular app in the browser
npm install && ng serve --open
 
