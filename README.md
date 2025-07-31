# MCP Host (MCP-LLM Bridge Service / ChatBot)

A Spring Boot service that acts as an intelligent bridge between Large Language Models (LLMs) and Model Context Protocol (MCP) clients. This service analyzes user intent and routes requests appropriately between direct LLM responses and MCP-based actions for Atlassian tools (Jira/Confluence).

## Architecture Overview

```
User Input → Intent Analysis → Route Decision
                ↓
    ┌─────────────────┬─────────────────┐
    ↓                 ↓                 ↓
LLM Only         MCP Jira         MCP Confluence
    ↓                 ↓                 ↓
DeepSeek API    MCP Client       MCP Client
    ↓                 ↓                 ↓
Direct Response  Jira Actions    Confluence Actions
```

## Key Components

### Intent Analysis
- **Rule-based**: Pattern matching for Jira/Confluence keywords
- **LLM-enhanced**: DeepSeek API for complex intent classification
- **Confidence scoring**: Routes to MCP only when confidence threshold is met

### Services
- **ChatOrchestrationService**: Main orchestration logic
- **IntentAnalysisService**: Determines user intent (Jira/Confluence/LLM-only)
- **DeepSeekService**: Handles LLM API communication
- **McpClientService**: Communicates with MCP client for Atlassian operations

### Models
- **ChatRequest/ChatResponse**: API contract
- **IntentAnalysisResult**: Intent classification with confidence
- **IntentType**: Enum for different intent categories

## Configuration

### Environment Variables
```bash
# DeepSeek API
DEEPSEEK_API_TOKEN=your_deepseek_token

# Optional API security
API_KEY=your_api_key

# MCP Client endpoint (default: http://localhost:3335)
MCP_CLIENT_URL=http://localhost:3335
```

### Application Properties
```properties
# Intent analysis threshold (0.0-1.0)
intent.confidence.threshold=0.7

# Service endpoints
mcp.client.url=http://localhost:3335
deepseek.api.url=https://api.deepseek.com

# Timeouts
mcp.client.timeout=30s
deepseek.api.timeout=30s
```

## API Endpoints

### POST /api/chat/message
Process a chat message with intent analysis and routing.

**Request:**
```json
{
  "message": "Create a new Jira issue for bug fix",
  "context": {},
  "userId": "user123"
}
```

**Response:**
```json
{
  "response": "I've created a new Jira issue for your bug fix request...",
  "intent": {
    "type": "MCP_JIRA",
    "confidence": 0.95,
    "service": "jira"
  },
  "mcpResult": {
    "success": true,
    "issueKey": "PROJ-123"
  },
  "success": true,
  "error": null
}
```

### GET /api/chat/health
Health check endpoint.

## Intent Types

- **MCP_JIRA**: Jira-related operations (issues, projects, sprints)
- **MCP_CONFLUENCE**: Confluence operations (pages, spaces, content)
- **LLM_ONLY**: General conversational queries
- **HYBRID**: Complex queries requiring both MCP and LLM (future)

## Integration Flow

1. **User sends message** → ChatController
2. **Intent analysis** → Determine if MCP action needed
3. **Route decision** based on confidence threshold:
   - High confidence → Execute MCP action + generate contextual response
   - Low confidence → Direct LLM response
4. **Response generation** → DeepSeek API for human-friendly explanations

## Development Setup

### Prerequisites
- Java 21+
- Gradle 8+
- Running MCP client (mcp-client project)
- DeepSeek API token

### Build and Run
```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# RUN Dev
./gradlew bootRun --continuous

# With custom properties
DEEPSEEK_API_TOKEN=your_token ./gradlew bootRun
```

### Testing
```bash
# Unit tests
./gradlew test

# Integration test with curl
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "Show me recent Jira issues", "userId": "test"}'
```

## Production Considerations

### Security
- Implement proper API authentication
- Restrict CORS origins
- Validate and sanitize all inputs
- Secure DeepSeek API token storage

### Monitoring
- Add comprehensive logging
- Implement metrics collection
- Set up health checks for dependencies
- Monitor API rate limits

### Scalability
- Consider caching for frequent queries
- Implement circuit breakers for external APIs
- Add request queuing for high load
- Scale horizontally with load balancers

## Future Enhancements

1. **Advanced Intent Analysis**
   - Machine learning models for better classification
   - Context-aware intent detection
   - Multi-turn conversation support

2. **Enhanced MCP Integration**
   - Dynamic tool discovery
   - Parameter extraction via LLM
   - Batch operations support

3. **User Experience**
   - Streaming responses
   - Rich formatting support
   - Interactive confirmations

4. **Enterprise Features**
   - User authentication/authorization
   - Audit logging
   - Rate limiting per user
   - Custom prompt templates

## Dependencies

- **Spring Boot 3.5.3**: Web framework and dependency injection
- **Spring WebFlux**: Reactive web stack
- **Jackson**: JSON processing
- **Jakarta Validation**: Request validation
- **Reactor**: Reactive programming

## License

[Add your license information here]
