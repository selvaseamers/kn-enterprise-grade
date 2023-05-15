#!/bin/bash


# Set the port numbers for the Spring Boot app and the Angular app
SPRING_BOOT_PORT=8080
ANGULAR_PORT=4200


# Kill the running processes on the specified ports
echo "Killing processes on ports ${SPRING_BOOT_PORT} and ${ANGULAR_PORT}..."
sudo kill $(sudo lsof -t -i:${SPRING_BOOT_PORT},${ANGULAR_PORT}) &> /dev/null

