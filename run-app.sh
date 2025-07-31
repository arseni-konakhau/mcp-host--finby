#!/bin/bash

# MCP-LLM Application Startup Script
# This script loads environment variables from .env file and starts the application

echo "🚀 Starting MCP-LLM Application..."

# Check if .env file exists
if [ ! -f ".env" ]; then
    echo "❌ Error: .env file not found!"
    echo "Please create a .env file with your DeepSeek API token."
    exit 1
fi

# Load environment variables from .env file
export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)

# Validate required environment variables
if [ -z "$DEEPSEEK_API_TOKEN" ]; then
    echo "❌ Error: DEEPSEEK_API_TOKEN not found in .env file!"
    exit 1
fi

echo "✅ Environment variables loaded"
echo "🔧 Building and starting application..."

# Start the application
./gradlew bootRun
