# System Patterns

- **Layered architecture:** Controller → Service → Model/Config
- **RESTful API design:** `/api/chat/message`, `/api/chat/health`
- **Reactive programming:** Uses Project Reactor (`Mono`)
- **External service integration:** DeepSeek LLM, MCP backend (Jira/Confluence)
- **Properties-based configuration:** All integration and system settings in `application.properties`
- **Error handling:** Fallbacks for LLM and MCP failures, user-friendly error messages
- **Testing:** JUnit, Spring Boot Test, Testcontainers
- **Build:** Gradle, Java toolchain (Java 21) 