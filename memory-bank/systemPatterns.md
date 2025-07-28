# MCP-LLM System Patterns

## Architecture Overview
MCP-LLM follows a layered microservice architecture with clear separation of concerns:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Client   │───▶│    MCP-LLM      │───▶│   MCP-Client    │
│                 │    │  (This Service) │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │                        │
                              ▼                        ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │  DeepSeek API   │    │ MCP-Server      │
                       │                 │    │  (Atlassian)    │
                       └─────────────────┘    └─────────────────┘
```

## Core Design Patterns

### 1. Orchestration Pattern
**ChatOrchestrationService** acts as the central coordinator:
- Receives user requests
- Delegates to appropriate services
- Combines results from multiple sources
- Returns unified responses

### 2. Strategy Pattern
**IntentAnalysisService** uses strategy pattern for intent detection:
- Analyzes user input using LLM
- Determines appropriate action type
- Extracts parameters for tool execution

### 3. Adapter Pattern
**McpClientService** adapts between:
- Internal service interfaces
- External MCP client API
- Handles protocol translation and error mapping

### 4. Configuration Pattern
Environment-driven configuration using Spring Boot properties:
- **DeepSeekProperties**: LLM API configuration
- **McpLlmProperties**: Application and MCP client settings
- Externalized configuration for different environments

## Service Layer Architecture

### Controller Layer
- **ChatController**: REST API endpoint
- Handles HTTP requests/responses
- Input validation and error handling
- API documentation and security

### Service Layer
- **ChatOrchestrationService**: Main business logic coordinator
- **IntentAnalysisService**: Natural language understanding
- **DeepSeekService**: LLM communication
- **McpClientService**: MCP tool execution

### Configuration Layer
- **WebClientConfiguration**: HTTP client setup
- **Properties classes**: Environment configuration
- **Security configuration**: API key validation

## Data Flow Patterns

### Request Processing Flow
1. **Input Validation**: Validate request format and authentication
2. **Intent Analysis**: Determine user intent using LLM
3. **Action Routing**: Route to appropriate service based on intent
4. **Tool Execution**: Execute MCP tools if required
5. **Response Synthesis**: Combine tool results with LLM insights
6. **Output Formatting**: Format response for client consumption

### Error Handling Pattern
- **Circuit Breaker**: Prevent cascade failures to external services
- **Retry Logic**: Automatic retry for transient failures
- **Graceful Degradation**: Fallback responses when services unavailable
- **Structured Error Responses**: Consistent error format across APIs

## Integration Patterns

### Reactive Programming
- **WebFlux**: Non-blocking I/O for better scalability
- **Mono/Flux**: Reactive streams for async operations
- **Timeout Handling**: Configurable timeouts for external calls

### External Service Integration
- **WebClient**: HTTP client for external API calls
- **Connection Pooling**: Efficient resource utilization
- **Load Balancing**: Support for multiple service instances

## Security Patterns

### API Security
- **API Key Authentication**: Optional security layer
- **Input Sanitization**: Prevent injection attacks
- **Rate Limiting**: Protect against abuse

### Configuration Security
- **Environment Variables**: Sensitive data externalization
- **Secret Management**: Secure handling of API tokens
- **Principle of Least Privilege**: Minimal required permissions

## Monitoring and Observability

### Logging Pattern
- **Structured Logging**: JSON format for log aggregation
- **Correlation IDs**: Track requests across services
- **Performance Metrics**: Response times and error rates

### Health Checks
- **Actuator Endpoints**: Spring Boot health monitoring
- **Dependency Checks**: Verify external service availability
- **Graceful Shutdown**: Clean service termination

## Scalability Patterns

### Horizontal Scaling
- **Stateless Design**: No server-side session state
- **Load Balancer Ready**: Multiple instance support
- **Database Connection Pooling**: Efficient resource usage

### Performance Optimization
- **Caching Strategy**: Cache frequently accessed data
- **Async Processing**: Non-blocking operations
- **Resource Pooling**: Efficient connection management
