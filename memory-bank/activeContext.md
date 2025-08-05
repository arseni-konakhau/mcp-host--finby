# Active Context

This project is a Java Spring Boot application for MCP LLM orchestration and chat services.
It exposes REST endpoints for chat and health checks, orchestrates intent analysis, and integrates with both LLM (DeepSeek) and MCP tools (Jira, Confluence).
The codebase is organized into configuration, controller, model, and service layers, with HTTP validation scripts and Gradle build setup.

## Key Components
- **Controller:** Handles REST API requests (`/api/chat/message`, `/api/chat/health`)
- **Service Layer:** Orchestrates chat, intent analysis, LLM and MCP tool integration
- **Model Layer:** Defines request/response and intent data structures
- **Configuration:** Manages properties, WebClient beans, and integration settings
- **External Integrations:** DeepSeek LLM API, MCP backend for Jira/Confluence
- **Build:** Gradle, Java 21, Spring Boot 3.5.3

---

## REST Endpoints

- **POST /api/chat/message**  
  Processes a chat message.  
  - Input: `ChatRequest` (validated)
  - Output: `ChatResponse` (wrapped in `ResponseEntity`)
  - Delegates to: `ChatOrchestrationService.processMessage()`

- **GET /api/chat/health**  
  Health check endpoint.  
  - Output: `"MCP-LLM Service is running"`

---

## Service Layer

- **ChatOrchestrationService:**  
  Orchestrates chat message processing, intent analysis, and integration with MCP tools (Jira, Confluence) and LLMs.  
  - Analyzes intent of incoming messages.
  - Routes to MCP (Jira/Confluence) tool execution or LLM-only response.
  - Handles hybrid intents, error fallback, and response formatting.
  - Extracts tool names and parameters from user messages for Jira/Confluence.
  - Formats and explains results using LLM if needed.

- **DeepSeekService:**  
  Handles communication with the DeepSeek LLM API.  
  - Generates LLM responses for chat and contextual explanations.
  - Provides intent analysis via LLM prompt.
  - Handles API errors and timeouts gracefully.

- **IntentAnalysisService:**  
  Detects user intent (Jira, Confluence, or LLM-only) using regex patterns and keyword scoring.
  - Returns intent type, confidence, and service (jira/confluence/llm).
  - Confidence is calculated based on keyword matches.

- **McpClientService:**  
  Integrates with MCP backend for Jira and Confluence tool execution.
  - Executes Jira/Confluence tools and actions via HTTP.
  - Handles errors and timeouts, returns error maps if needed.
  - Can fetch available tools from MCP backend.

---

## Model Layer

- **ChatRequest:** Represents incoming chat messages.
- **ChatResponse:** Represents responses, including LLM/MCP results, intent, and error info.
- **IntentAnalysisResult:** Holds intent type, confidence, and service.
- **IntentType:** Enum for intent types (MCP, LLM, etc.).

---

## Configuration

- **McpLlmProperties:** Holds configuration for MCP/LLM integration (e.g., confidence thresholds, timeouts).
- **DeepSeekProperties:** Holds DeepSeek LLM API config (URL, token, timeout).
- **WebClientConfiguration:** Configures WebClient beans for HTTP calls to DeepSeek and MCP services.
- **application.properties:** Stores property values for the above configs.

---

## External Integrations

- **DeepSeek LLM:** Used for generating chat responses and explanations.
- **MCP Backend:** Used for executing Jira and Confluence tools/actions.
- **HTTP Validation Scripts:** Present in `_http/` for testing endpoints and integrations.
