# MCP-LLM Project Brief

## Project Overview
MCP-LLM is a Spring Boot application that serves as an intelligent bridge between Large Language Models (LLMs) and Model Context Protocol (MCP) clients. It enables natural language interaction with structured MCP tools, specifically for Jira and Confluence operations.

## Core Purpose
Transform natural language user requests into structured MCP tool executions while maintaining conversational context and providing intelligent responses.

## Key Requirements

### Primary Goals
1. **LLM Integration**: Connect to DeepSeek API for natural language processing
2. **Intent Analysis**: Analyze user messages to determine if MCP actions are required
3. **MCP Client Communication**: Interface with existing mcp-client for tool execution
4. **Chat Orchestration**: Coordinate between LLM responses and MCP operations
5. **API Gateway**: Provide REST endpoint for chat interactions

### Technical Requirements
- Java Spring Boot application
- Reactive programming with WebFlux
- Environment-based configuration
- Robust error handling and timeouts
- Structured JSON API responses

### Integration Points
- **mcp-client**: Java-based MCP client for Jira/Confluence operations
- **mcp-server--atlassian**: Python MCP server providing Atlassian tools
- **DeepSeek API**: LLM service for natural language processing

## Success Criteria
1. Users can interact with Jira/Confluence through natural language
2. System correctly identifies when MCP actions are needed
3. Seamless integration between conversational AI and structured tool execution
4. Reliable error handling and graceful degradation
5. Configurable and deployable architecture

## Architecture Principles
- Microservice architecture with clear separation of concerns
- Reactive programming for non-blocking operations
- Configuration-driven deployment
- Comprehensive logging and monitoring capabilities
- Extensible design for additional LLM providers and MCP tools
