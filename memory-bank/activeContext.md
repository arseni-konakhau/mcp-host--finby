# Active Context

This project is a Java Spring Boot application for MCP LLM input routing and recognition services.
It exposes a REST endpoint that intercepts user input, determines whether it should be routed to MCP communication (JIRA/Confluence) or indicated as a regular LLM request, and returns an ENUM-based routing decision.
The system is consumed by other applications via HTTP and provides actual MCP responses for JIRA/Confluence requests while only indicating LLM intent without calling DeepSeek.

## Key Components
- **Controller:** Handles REST API requests for input routing (`/api/chat/message`, `/api/chat/health`)
- **Service Layer:** Orchestrates intent analysis and MCP tool integration
- **Model Layer:** Defines request/response and intent data structures with ENUM-based routing decisions
- **Configuration:** Manages properties, WebClient beans, and integration settings
- **External Integrations:** MCP-Client for JIRA/Confluence, DeepSeek (indication only, no actual calls)
- **Build:** Gradle, Java 21, Spring Boot 3.5.3

---

## REST Endpoints

- **POST /api/chat/message**  
  Processes user input and determines routing decision.  
  - Input: `ChatRequest` (user input)
  - Output: `ChatResponse` with ENUM indication (MCP or LLM)
  - For MCP: Provides actual MCP response via MCP-Client
  - For LLM: Only indicates LLM intent (no DeepSeek call)
  - Delegates to: `ChatOrchestrationService.processMessage()`

- **GET /api/chat/health**  
  Health check endpoint.  
  - Output: `"MCP-LLM Service is running"`

---

## Service Layer

- **ChatOrchestrationService:**  
  Orchestrates input processing, intent analysis, and routing decisions.  
  - Analyzes intent of incoming user input.
  - Routes to MCP-Client for JIRA/Confluence requests (provides actual response).
  - Returns LLM indication for generic requests (no DeepSeek call).
  - Handles error fallback and response formatting.
  - Extracts tool names and parameters from user messages for JIRA/Confluence.

- **IntentAnalysisService:**  
  Detects user intent (JIRA, Confluence, or LLM-only) using regex patterns and keyword scoring.
  - Returns intent type, confidence, and service (jira/confluence/llm).
  - Confidence is calculated based on keyword matches.
  - **Current Limitation:** Recognition logic needs improvement for better JIRA/Confluence detection.

- **McpClientService:**  
  Integrates with MCP-Client backend for JIRA and Confluence tool execution.
  - Executes JIRA/Confluence tools and actions via HTTP.
  - Handles errors and timeouts, returns error maps if needed.
  - Provides actual MCP responses for JIRA/Confluence requests.

- **DeepSeekService:**  
  **Note:** Currently not used for actual LLM responses, only for indication purposes.
  - Previously handled communication with DeepSeek LLM API.
  - Now only used for intent analysis via LLM prompt if needed.

---

## Model Layer

- **ChatRequest:** Represents incoming user input.
- **ChatResponse:** Represents routing decisions and responses, including MCP results and LLM indication.
- **IntentAnalysisResult:** Holds intent type, confidence, and service.
- **IntentType:** Enum for intent types (MCP, LLM).

---

## Configuration

- **McpLlmProperties:** Holds configuration for MCP integration and intent analysis (confidence thresholds, timeouts).
- **DeepSeekProperties:** Holds DeepSeek LLM API config (currently unused for actual LLM calls).
- **WebClientConfiguration:** Configures WebClient beans for HTTP calls to MCP-Client.
- **application.properties:** Stores property values for the above configs.

---

## External Integrations

- **MCP-Client:** Core service for executing JIRA and Confluence tools/actions.
- **DeepSeek LLM:** Currently unused for actual responses, only for indication.
- **HTTP Validation Scripts:** Present in `_http/` for testing endpoints and integrations.
- **Consuming Applications:** Other applications that consume this service via HTTP for routing decisions.

---

## Current MVP Status

- **Working Demo:** Basic functionality operational for simple demonstration scenarios
- **Recognition Challenge:** Need to improve JIRA/Confluence detection accuracy
- **Deployment:** JAR-based deployment on UNIX environment with .env configuration
- **Integration:** Successfully consumed by other applications via HTTP API
