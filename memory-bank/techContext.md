# MCP-LLM Technical Context

## Technology Stack

### Core Framework
- **Java 17**: Modern Java features and performance improvements
- **Spring Boot 3.x**: Latest Spring framework with native compilation support
- **Spring WebFlux**: Reactive web framework for non-blocking I/O
- **Gradle**: Build automation and dependency management

### Key Dependencies
- **Spring Boot Starter WebFlux**: Reactive web applications
- **Spring Boot Starter Actuator**: Production monitoring and management
- **Jackson**: JSON serialization/deserialization
- **Reactor Core**: Reactive programming primitives
- **Spring Boot Test**: Testing framework integration

### External Integrations
- **DeepSeek API**: LLM service for natural language processing
- **MCP Client**: Java-based client for MCP protocol communication
- **MCP Server (Atlassian)**: Python-based server providing Jira/Confluence tools

## Development Environment

### Build Configuration
```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.3'
    id 'io.spring.dependency-management' version '1.1.7'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

### Project Structure
```
src/
├── main/java/com/mcpllm/mcpllm/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── model/          # Data models and DTOs
│   ├── service/        # Business logic services
│   └── McpLlmApplication.java
├── main/resources/
│   └── application.properties
└── test/java/          # Test classes
```

## Configuration Management

### Environment Variables
- `DEEPSEEK_API_TOKEN`: Authentication token for DeepSeek API
- `MCP_CLIENT_URL`: Base URL for MCP client service (default: http://localhost:3332)
- `API_KEY`: Optional API key for securing endpoints
- `SERVER_PORT`: Application server port (default: 8080)

### Application Properties
```properties
# Server configuration
server.port=${SERVER_PORT:8080}

# DeepSeek API configuration
deepseek.api.token=${DEEPSEEK_API_TOKEN}
deepseek.api.base-url=https://api.deepseek.com
deepseek.api.timeout=30s

# MCP Client configuration
mcp-llm.client.base-url=${MCP_CLIENT_URL:http://localhost:3332}
mcp-llm.client.timeout=30s

# Security configuration
mcp-llm.security.api-key=${API_KEY:}
```

## Development Constraints

### Java Version Requirements
- Minimum Java 17 for modern language features
- Spring Boot 3.x requires Java 17+
- Native compilation support available

### Memory and Performance
- Reactive programming for better resource utilization
- WebClient for non-blocking HTTP calls
- Configurable timeouts for external service calls
- Connection pooling for efficient resource usage

### Security Considerations
- Environment-based configuration for sensitive data
- Optional API key authentication
- Input validation and sanitization
- Secure handling of external API tokens

## Testing Strategy

### Unit Testing
- JUnit 5 for test framework
- Mockito for mocking dependencies
- WebTestClient for reactive web testing
- TestContainers for integration testing

### Integration Testing
- Spring Boot Test for application context testing
- MockWebServer for external API mocking
- Test profiles for different environments

## Deployment Considerations

### Containerization
- Docker support with multi-stage builds
- Optimized for container environments
- Health check endpoints for orchestration

### Monitoring and Observability
- Spring Boot Actuator endpoints
- Structured logging with correlation IDs
- Metrics collection for performance monitoring
- Health checks for dependency services

### Scalability
- Stateless application design
- Horizontal scaling support
- Load balancer compatibility
- Resource-efficient reactive architecture

## Development Tools

### IDE Configuration
- IntelliJ IDEA or VS Code recommended
- Java 17 SDK configuration
- Gradle integration
- Spring Boot development tools

### Code Quality
- Standard Java coding conventions
- Spring Boot best practices
- Reactive programming patterns
- Comprehensive error handling

## External Service Dependencies

### DeepSeek API
- RESTful HTTP API
- JSON request/response format
- Rate limiting considerations
- Authentication via API token

### MCP Client Service
- Java-based HTTP service
- RESTful API for tool execution
- JSON-based communication protocol
- Timeout and retry handling

### MCP Server (Atlassian)
- Python-based MCP server
- Provides Jira and Confluence tools
- Accessed through MCP client
- Authentication handled by MCP client
