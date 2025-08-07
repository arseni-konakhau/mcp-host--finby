# MCP LLM - Deployment Guide

## Prerequisites
- Java 21 JDK (OpenJDK or Oracle) - Current: 21.0.5 (Temurin)
- Git
- Gradle 8.14.3 (included via wrapper)
- DeepSeek API token (for DEEPSEEK_API_TOKEN environment variable)
- Running MCP Client instance (for MCP_CLIENT_URL)

## Detailed Deployment

1. Clone repository:
```bash
git clone https://github.com/arseni-konakhau/mcp-llm.git
cd mcp-llm
```

2. Create .env file with required variables:
```bash
cp ./_config_/env.selectel ./.env # or use your specific env variables
```

3. Run with automated setup:
```bash
chmod +x start.sh
bash ./start.sh
```

4. Verify:
```bash
# Check if process is running:
ps aux | grep mcp-llm

# Check application health:
curl http://localhost:3336/actuator/health
# Should return {"status":"UP"}
```

5. Stop Process:
```bash
pkill -f mcp-llm
```

## Key Details
- Runs on port 3336
- Requires Java 21.0.5 (Temurin)
- Uses Gradle 8.14.3 via wrapper
- Logs to: app.log
- Requires environment variables in .env file:
  - DEEPSEEK_API_TOKEN
  - MCP_CLIENT_URL
