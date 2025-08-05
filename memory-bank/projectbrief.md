# Project Brief

This project implements an MCP LLM input routing and recognition service using Java Spring Boot.
It provides a RESTful endpoint that intercepts user input and determines whether it should be routed to MCP communication (for JIRA/Confluence-related requests) or indicated as a regular LLM request.
The system is designed for simple deployment, clear routing logic, and integration with external consuming applications via HTTP.

## Core Functionality
- **Input Interception:** Receives user input via HTTP POST
- **Intent Recognition:** Determines if input is MCP-related (JIRA/Confluence) or generic LLM request
- **Routing Decision:** Returns ENUM indication (MCP or LLM) for consuming applications
- **MCP Integration:** Provides actual MCP responses for JIRA/Confluence requests
- **LLM Indication:** Only indicates LLM intent without calling DeepSeek (no actual LLM response)

## Current Status
- **MVP Level:** Working solution for simple demo scenarios
- **Recognition:** Basic keyword/pattern matching for JIRA/Confluence detection
- **Deployment:** JAR-based deployment on UNIX environment with .env configuration
- **Integration:** Consumed by other applications via HTTP API 