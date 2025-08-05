# Product Context

## Primary Purpose
- **Input Routing Service:** Intercepts user input and determines routing between MCP and LLM flows
- **Intent Recognition:** Analyzes user input to detect JIRA/Confluence-related requests vs generic LLM requests
- **MCP Integration:** Provides actual MCP responses for JIRA/Confluence requests via MCP-Client
- **LLM Indication:** Returns indication for LLM requests without calling DeepSeek (no actual LLM response)

## Key Features
- **Single Endpoint API:** RESTful endpoint for input processing and routing decisions
- **ENUM-based Response:** Clear indication of MCP vs LLM routing decision
- **MCP-Client Integration:** Core service for handling JIRA/Confluence MCP flows
- **Simple Recognition Logic:** Keyword/pattern matching for intent detection
- **HTTP Consumption:** Designed to be consumed by other applications via HTTP

## Current MVP Scope
- **Working Demo:** Basic functionality for simple demonstration scenarios
- **Recognition Improvement Needed:** Better detection of JIRA/Confluence-related input
- **Deployment:** JAR-based deployment on UNIX environment with .env configuration
- **Integration Point:** HTTP API consumed by external applications for routing decisions

## Future Enhancements
- **Advanced Recognition:** Potential NLP integration for better intent detection
- **Expanded MCP Support:** Additional MCP tools beyond JIRA/Confluence
- **Monitoring & Logging:** Enhanced tracking of routing decisions and recognition accuracy 