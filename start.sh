#!/bin/bash

# Verify Java 21 is installed
echo "Verifying Java 21..."
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "$JAVA_VERSION" != "21"* ]]; then
  echo "ERROR: Java 21 is required. Found version: $JAVA_VERSION"
  exit 1
fi
echo "Java version OK: $JAVA_VERSION"

# Verify Gradle wrapper
echo "Verifying Gradle wrapper..."
if [ ! -f "./gradlew" ]; then
  echo "ERROR: gradlew wrapper not found"
  exit 1
fi

# Check if .env exists
if [ ! -f .env ]; then
  echo "ERROR: .env file not found"
  echo "Required variables: DEEPSEEK_API_TOKEN, MCP_CLIENT_URL, SERVER_PORT"
  exit 1
fi

# Build project
echo "Building project..."
./gradlew clean build
if [ $? -ne 0 ]; then
  echo "ERROR: Build failed"
  exit 1
fi

# Source environment variables
source .env
SERVER_PORT=${SERVER_PORT:-3336}
SERVER_ADDRESS=${SERVER_ADDRESS}

echo "Starting application on ${SERVER_ADDRESS}:${SERVER_PORT}..."
mkdir -p logs
nohup java -jar build/libs/mcp-llm-0.0.1-SNAPSHOT.jar > logs/mcp-llm.log 2>&1 &
echo $! > mcp-llm.pid

# Verify startup
echo "Validating startup..."
sleep 5
curl -s https://${SERVER_ADDRESS}:${SERVER_PORT}/actuator/health | grep -q '"status":"UP"'
if [ $? -eq 0 ]; then
  echo "Application is running successfully on ${SERVER_ADDRESS}:${SERVER_PORT}"
  echo "Access: https://${SERVER_ADDRESS}:${SERVER_PORT}"
else
  echo "ERROR: Application failed to start (valid only for dev mode, so please check on public access for following address: https://${SERVER_ADDRESS}:${SERVER_PORT})"
  exit 1
fi
