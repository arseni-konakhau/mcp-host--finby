# Tech Context

- **Java:** 21 (via Gradle toolchain)
- **Spring Boot:** 3.5.3
- **Gradle:** Build system
- **Dependencies:**
  - spring-boot-starter-web
  - spring-boot-starter-webflux
  - spring-boot-starter-actuator
  - spring-boot-starter-validation
  - com.fasterxml.jackson (JSON)
  - spring-boot-devtools (dev only)
  - spring-boot-starter-test, spring-boot-testcontainers, junit-platform-launcher (test)
- **Reactive stack:** WebFlux, Reactor
- **External APIs:** DeepSeek LLM, MCP backend (Jira/Confluence)
- **Configuration:** All major settings in `application.properties`
- **HTTP validation scripts:** Present in `_http/` for endpoint testing

---

## application.properties (Key Settings)
- `spring.application.name=mcp-llm`
- `server.port=3336`
- `mcp.client.url=http://localhost:3335`
- `mcp.client.timeout=30s`
- `deepseek.api-url=https://api.deepseek.com`
- `deepseek.api-token=***`
- `deepseek.timeout=30s`
- `mcp.intent.confidence-threshold=0.7`
- `mcp.intent.mcp-enabled=true`
- `mcp.intent.llm-enabled=true`
- `mcp.security.api-key=...`
- `http.max.connections=20`
- `http.connection.timeout=10s`
- `http.read.timeout=30s`
- `management.endpoints.web.exposure.include=health,info`
- `management.endpoint.health.show-details=always`
- `logging.level.com.mcpllm=INFO`
- `logging.level.org.springframework.web.reactive.function.client=DEBUG` 