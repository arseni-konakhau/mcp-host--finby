You're absolutely right! Here's the simplified guide:

## Simple Production Run Guide

### Step 1: Build the JAR
```bash
./gradlew clean build
```

### Step 2: Create .env file
Create a `.env` file in your project root:
```bash
DEEPSEEK_API_TOKEN=my-ds-token
MCP_CLIENT_URL=http://localhost:3335
SERVER_PORT=3336
```

### Step 3: Run the JAR
```bash
# Load environment variables and run
source .env && java -jar build/libs/mcp-llm-0.0.1-SNAPSHOT.jar
```

### Step 4: Test
```bash
# Health check
curl http://localhost:3336/actuator/health

# Test chat endpoint
curl -X POST http://localhost:3336/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello", "userId": "test"}'
```

That's it! Your app will be running on port 3336. The `DEEPSEEK_API_TOKEN` is kept just to make the app compile and run in its current state.