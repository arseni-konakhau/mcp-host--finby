# MCP-LLM Development Guide

## Quick Start

The MCP-LLM application is now fully configured and ready for development. Here's how to run it:

### Prerequisites
- Java 17+ installed
- DeepSeek API token (already configured in `.env`)
- MCP Client service (optional, for full MCP functionality)

### Running the Application

#### Option 1: Using the convenience script (Recommended)
```bash
./run-app.sh
```

#### Option 2: Manual startup
```bash
# Load environment variables and run
export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)
./gradlew bootRun
```

#### Option 3: Direct command with environment variables
```bash
DEEPSEEK_API_TOKEN=sk-f2cd9b03c8fa428d91b4e37c535030ff SERVER_PORT=3334 ./gradlew bootRun
```

### Application Endpoints

Once running, the application will be available at:

- **Main Application**: http://localhost:3334
- **Health Check**: http://localhost:3334/actuator/health
- **Chat Health**: http://localhost:3334/api/chat/health
- **Chat API**: http://localhost:3334/api/chat/message (POST)

### Testing the Application

#### Health Check
```bash
curl http://localhost:3334/actuator/health
```

#### Chat API Test
```bash
curl -X POST http://localhost:3334/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, can you help me test this application?"}'
```

Expected response:
```json
{
  "response": "Of course! I'd be happy to help you test your application...",
  "intent": {
    "type": "LLM_ONLY",
    "confidence": 0.9,
    "service": "llm"
  },
  "mcpResult": null,
  "success": true,
  "error": null
}
```

### Configuration

The application uses the following configuration files:

- **`.env`**: Environment variables (DeepSeek API token, ports, etc.)
- **`src/main/resources/application.properties`**: Spring Boot configuration
- **`src/test/resources/application-test.properties`**: Test configuration

### Key Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `DEEPSEEK_API_TOKEN` | DeepSeek API token (required) | - |
| `SERVER_PORT` | Application port | 3334 |
| `MCP_CLIENT_URL` | MCP client service URL | http://localhost:3332 |
| `API_KEY` | Optional API security key | - |

### Development Notes

1. **Environment Variables**: The application requires `DEEPSEEK_API_TOKEN` to be set
2. **Port Configuration**: Application runs on port 3334 by default
3. **MCP Integration**: Connects to MCP client on port 3332 (optional)
4. **Hot Reload**: Spring Boot DevTools enabled for development
5. **Logging**: Debug logging enabled for HTTP client requests

### Troubleshooting

#### Application won't start
- Check that `DEEPSEEK_API_TOKEN` is set in `.env` file
- Verify Java 17+ is installed: `java -version`
- Ensure port 3334 is available

#### API requests fail
- Verify application is running: `curl http://localhost:3334/actuator/health`
- Check correct endpoint: `/api/chat/message` (not `/api/chat`)
- Ensure Content-Type header is set to `application/json`

#### Environment variable issues
- Use the `run-app.sh` script which handles environment loading
- Verify `.env` file format (no markdown formatting)
- Check for typos in environment variable names

### Next Steps

The application is now ready for:
- Integration with MCP client service
- Additional endpoint development
- Frontend integration
- Production deployment preparation

For more detailed information, see the memory bank documentation in the `memory-bank/` directory.
