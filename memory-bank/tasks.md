# Current Tasks

## Completed Analysis
- [x] Analyzed workspace structure and application architecture
- [x] Identified core functionality as input routing and recognition service
- [x] Documented current MVP status and limitations
- [x] Updated memory bank with current understanding

## Current Application Understanding

### Core Functionality
- **Input Routing Service:** Intercepts user input via HTTP POST
- **Intent Recognition:** Determines if input is MCP-related (JIRA/Confluence) or generic LLM request
- **Response Handling:** 
  - For MCP: Provides actual MCP response via MCP-Client
  - For LLM: Only indicates LLM intent (no DeepSeek call)
- **ENUM-based Output:** Returns clear indication of routing decision

### Current MVP Status
- **Working Demo:** Basic functionality operational for simple scenarios
- **Recognition Logic:** Basic keyword/pattern matching for JIRA/Confluence detection
- **Deployment:** JAR-based deployment on UNIX environment with .env configuration
- **Integration:** Consumed by other applications via HTTP API

### Identified Challenges
- **Recognition Accuracy:** Need to improve JIRA/Confluence detection
- **Current Logic:** Simple keyword matching may miss edge cases
- **Future Enhancement:** Potential NLP integration for better intent detection

## Next Steps for MVP Enhancement

### Immediate Improvements
- [ ] Audit current keyword/pattern lists for JIRA/Confluence detection
- [ ] Expand recognition patterns for better coverage
- [ ] Test with realistic JIRA/Confluence and generic LLM queries
- [ ] Document API and routing logic for consuming applications

### Optional Future Enhancements
- [ ] Research lightweight NLP libraries for better intent detection
- [ ] Implement logging for ambiguous cases
- [ ] Design extensible recognition logic (pluggable strategy pattern)
- [ ] Add monitoring for routing decision accuracy

## Architecture Summary

| Component | Technology/Pattern | Purpose |
|-----------|-------------------|---------|
| API Layer | Spring Boot Controller | Accepts user input via HTTP |
| Recognition Logic | Keyword/NLP, Enum Output | Determines MCP vs LLM |
| MCP Integration | MCP-Client Service | Handles JIRA/Confluence flows |
| LLM Indication | Enum Only (no LLM call) | Indicates LLM, no DeepSeek call |
| Config | .env, application.properties | Environment-specific settings |
| Deployment | JAR on UNIX | Simple execution |
| Consumption | HTTP by other apps | Integration point | 