# MCP LLM - Deployment Guide

## Prerequisites
- Git
- Java 21 JDK (OpenJDK or Oracle) - Current: 21.0.5 (Temurin)
- Gradle 8.14.3 (included via wrapper)
- Running MCP Client instance (for MCP_CLIENT_URL)



## Detailed Deployment

1. Clone repository:
```bash
git clone https://github.com/arseni-konakhau/mcp-llm.git
cd mcp-llm
```



2. Environment Variables:

```bash
cp ./_config_/env.selectel ./.env # example for intermediate target on selectel cloud
```

__NOTE: make sure you are using ur specific environment config based on target environment__



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
