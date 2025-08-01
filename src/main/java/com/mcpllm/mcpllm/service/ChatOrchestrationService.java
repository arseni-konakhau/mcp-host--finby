package com.mcpllm.mcpllm.service;

import com.mcpllm.mcpllm.config.McpLlmProperties;
import com.mcpllm.mcpllm.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatOrchestrationService {

    private final IntentAnalysisService intentAnalysisService;
    private final DeepSeekService deepSeekService;
    private final McpClientService mcpClientService;
    private final McpLlmProperties properties;

    public ChatOrchestrationService(IntentAnalysisService intentAnalysisService,
                                   DeepSeekService deepSeekService,
                                   McpClientService mcpClientService,
                                   McpLlmProperties properties) {
        this.intentAnalysisService = intentAnalysisService;
        this.deepSeekService = deepSeekService;
        this.mcpClientService = mcpClientService;
        this.properties = properties;
    }

    public Mono<ChatResponse> processMessage(ChatRequest request) {
        return intentAnalysisService.analyzeIntent(request.message())
            .flatMap(intent -> {
                if (intent.confidence() >= properties.intent().confidenceThreshold()) {
                    return processWithMcp(request, intent);
                } else {
                    return processWithLlmOnly(request, intent);
                }
            })
            .onErrorResume(throwable -> {
                return Mono.just(new ChatResponse(
                    "I apologize, but I encountered an error processing your request.",
                    null,
                    null,
                    false,
                    throwable.getMessage()
                ));
            });
    }

    private Mono<ChatResponse> processWithMcp(ChatRequest request, IntentAnalysisResult intent) {
        return switch (intent.type()) {
            case MCP -> {
                if ("jira".equals(intent.service())) {
                    yield processJiraRequest(request, intent);
                } else if ("confluence".equals(intent.service())) {
                    yield processConfluenceRequest(request, intent);
                }
                yield processWithLlmOnly(request, intent);
            }
            case HYBRID -> processHybridRequest(request, intent);
            default -> processWithLlmOnly(request, intent);
        };
    }

    private Mono<ChatResponse> processJiraRequest(ChatRequest request, IntentAnalysisResult intent) {
        // Extract action and parameters for Jira
        String toolName = extractJiraToolName(request.message());
        Map<String, Object> parameters = extractJiraParameters(request.message(), toolName);

        return mcpClientService.executeJiraTool(toolName, parameters)
            .map(mcpResult -> {
                if ((Boolean) mcpResult.getOrDefault("isError", true) == false) {
                    // Generate simple response from MCP data
                    String response = formatJiraResults(mcpResult);
                    return new ChatResponse(response, intent, mcpResult, true, null);
                } else {
                    return new ChatResponse(
                        "I couldn't retrieve JIRA data at the moment. Please try again later.",
                        intent, mcpResult, false, "MCP execution failed"
                    );
                }
            })
            .onErrorResume(throwable -> {
                // If MCP fails, return error response
                return Mono.just(new ChatResponse(
                    "I encountered an error while accessing JIRA. Please try again later.",
                    intent, null, false, throwable.getMessage()
                ));
            });
    }

    private Mono<ChatResponse> processConfluenceRequest(ChatRequest request, IntentAnalysisResult intent) {
        String toolName = extractConfluenceToolName(request.message());
        Map<String, Object> parameters = extractConfluenceParameters(request.message(), toolName);

        return mcpClientService.executeConfluenceTool(toolName, parameters)
            .flatMap(mcpResult -> {
                if ((Boolean) mcpResult.getOrDefault("isError", true) == false) {
                    return generateContextualResponse(request.message(), mcpResult, "confluence")
                        .map(response -> new ChatResponse(response, intent, mcpResult, true, null));
                } else {
                    return processWithLlmOnly(request, intent);
                }
            })
            .onErrorResume(throwable -> {
                // If MCP fails, fall back to LLM-only response
                return processWithLlmOnly(request, intent);
            });
    }

    private Mono<ChatResponse> processHybridRequest(ChatRequest request, IntentAnalysisResult intent) {
        // For MVP, treat hybrid as LLM-only
        return processWithLlmOnly(request, intent);
    }

    private Mono<ChatResponse> processWithLlmOnly(ChatRequest request, IntentAnalysisResult intent) {
        return Mono.just(new ChatResponse("", intent, null, true, null));
    }

    private Mono<String> generateContextualResponse(String userMessage, Map<String, Object> mcpResult, String service) {
        String systemPrompt = String.format("""
            You are an AI assistant that helps users understand the results of %s operations.
            
            The user asked: "%s"
            
            The operation result was: %s
            
            Provide a clear, human-friendly explanation of what happened and the results.
            Be concise but informative.
            """, service, userMessage, mcpResult.toString());

        return deepSeekService.generateResponse("Explain the result", systemPrompt)
            .onErrorReturn(generateFallbackResponse(userMessage, mcpResult, service));
    }
    
    private String generateFallbackResponse(String userMessage, Map<String, Object> mcpResult, String service) {
        // Provide a simple fallback response when DeepSeek is not available
        if (service.equals("jira")) {
            return String.format("Here are the JIRA results for your query '%s':\n\n%s", 
                userMessage, formatJiraResults(mcpResult));
        } else if (service.equals("confluence")) {
            return String.format("Here are the Confluence results for your query '%s':\n\n%s", 
                userMessage, formatConfluenceResults(mcpResult));
        }
        return String.format("Operation completed successfully. Results: %s", mcpResult.toString());
    }
    
    private String formatJiraResults(Map<String, Object> mcpResult) {
        // Simple formatting for JIRA results
        Object content = mcpResult.get("content");
        if (content != null && content instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> contentList = (java.util.List<Map<String, Object>>) content;
            if (!contentList.isEmpty()) {
                Map<String, Object> firstItem = contentList.get(0);
                String text = (String) firstItem.get("text");
                if (text != null) {
                    try {
                        // Parse the JSON text to extract issue information
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        Map<String, Object> data = mapper.readValue(text, Map.class);
                        @SuppressWarnings("unchecked")
                        java.util.List<Map<String, Object>> issues = (java.util.List<Map<String, Object>>) data.get("issues");
                        
                        if (issues != null && !issues.isEmpty()) {
                            StringBuilder result = new StringBuilder();
                            result.append("Found ").append(issues.size()).append(" JIRA issue(s):\n\n");
                            
                            for (Map<String, Object> issue : issues) {
                                String key = (String) issue.get("key");
                                String summary = (String) issue.get("summary");
                                @SuppressWarnings("unchecked")
                                Map<String, Object> status = (Map<String, Object>) issue.get("status");
                                String statusName = status != null ? (String) status.get("name") : "Unknown";
                                
                                result.append("• ").append(key).append(": ").append(summary)
                                    .append(" [Status: ").append(statusName).append("]\n");
                            }
                            
                            return result.toString();
                        }
                    } catch (Exception e) {
                        // Fall back to raw content if parsing fails
                        return "JIRA Results:\n" + text;
                    }
                }
            }
        }
        return "JIRA operation completed successfully.";
    }
    
    private String formatConfluenceResults(Map<String, Object> mcpResult) {
        // Simple formatting for Confluence results
        Object content = mcpResult.get("content");
        if (content != null) {
            return content.toString();
        }
        return "Confluence operation completed successfully.";
    }

    // Extract appropriate MCP tool name for Jira operations
    private String extractJiraToolName(String message) {
        String lower = message.toLowerCase();
        
        // Specific issue lookup
        if (lower.matches(".*\\b(smp|proj|dev|test)-\\d+\\b.*")) {
            return "jira_get_issue";
        }
        
        // User-specific queries
        if (lower.contains("assigned to me") || lower.contains("my issues")) {
            return "jira_search";
        }
        
        // Project queries
        if (lower.contains("project") && (lower.contains("issues") || lower.contains("tickets"))) {
            return "jira_get_project_issues";
        }
        
        // Sprint queries
        if (lower.contains("sprint") || lower.contains("board")) {
            return "jira_get_agile_boards";
        }
        
        // Create operations
        if (lower.contains("create") || lower.contains("new")) {
            return "jira_create_issue";
        }
        
        // Default to search for most queries
        return "jira_search";
    }

    private String extractConfluenceToolName(String message) {
        String lower = message.toLowerCase();
        
        // Page creation
        if (lower.contains("create") || lower.contains("new page")) {
            return "confluence_create_page";
        }
        
        // Specific page lookup
        if (lower.contains("get") && lower.contains("page")) {
            return "confluence_get_page";
        }
        
        // Default to search
        return "confluence_search";
    }

    private Map<String, Object> extractJiraParameters(String message, String toolName) {
        String lower = message.toLowerCase();
        Map<String, Object> params = new HashMap<>();
        
        switch (toolName) {
            case "jira_search" -> {
                // Build JQL query based on message content
                StringBuilder jql = new StringBuilder();
                
                // Extract project if mentioned
                if (lower.contains("project smp") || lower.contains("smp")) {
                    jql.append("project = SMP");
                }
                
                // Add status filters
                if (lower.contains("open") || lower.contains("active")) {
                    if (jql.length() > 0) jql.append(" AND ");
                    jql.append("status != Done AND status != Closed");
                }
                
                // Add assignee filters
                if (lower.contains("assigned to me") || lower.contains("my issues")) {
                    if (jql.length() > 0) jql.append(" AND ");
                    jql.append("assignee = currentUser()");
                }
                
                // Default query if nothing specific
                if (jql.length() == 0) {
                    jql.append("project = SMP ORDER BY created DESC");
                }
                
                params.put("jql", jql.toString());
                params.put("limit", 10);
            }
            case "jira_get_issue" -> {
                // Extract issue key from message
                String issueKey = extractIssueKey(message);
                if (issueKey != null) {
                    params.put("issue_key", issueKey);
                }
            }
            case "jira_get_project_issues" -> {
                params.put("project_key", "SMP");
                params.put("limit", 10);
            }
            case "jira_create_issue" -> {
                params.put("project_key", "SMP");
                params.put("summary", extractSummaryFromMessage(message));
                params.put("issue_type", "Task");
                if (lower.contains("bug")) {
                    params.put("issue_type", "Bug");
                }
            }
            default -> {
                params.put("jql", "project = SMP ORDER BY created DESC");
                params.put("limit", 10);
            }
        }
        
        return params;
    }

    private Map<String, Object> extractConfluenceParameters(String message, String toolName) {
        Map<String, Object> params = new HashMap<>();
        
        switch (toolName) {
            case "confluence_search" -> {
                params.put("query", extractSearchTerms(message));
                params.put("limit", 10);
            }
            case "confluence_get_page" -> {
                String pageTitle = extractPageTitle(message);
                if (pageTitle != null) {
                    params.put("title", pageTitle);
                    params.put("space_key", "DEV"); // Default space
                }
            }
            case "confluence_create_page" -> {
                params.put("space_key", "DEV");
                params.put("title", extractPageTitle(message));
                params.put("content", "# " + extractPageTitle(message) + "\n\nPage created via MCP-LLM");
            }
            default -> {
                params.put("query", message);
                params.put("limit", 10);
            }
        }
        
        return params;
    }
    
    private String extractIssueKey(String message) {
        // Look for patterns like SMP-123, PROJ-456, etc.
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b([A-Z]+)-\\d+\\b");
        java.util.regex.Matcher matcher = pattern.matcher(message.toUpperCase());
        if (matcher.find()) {
            return matcher.group(0); // Return the full match (e.g., "SMP-1"), not just the project key
        }
        return null;
    }
    
    private String extractSummaryFromMessage(String message) {
        // Extract summary from create requests
        String lower = message.toLowerCase();
        if (lower.contains("title") && lower.contains("'")) {
            int start = lower.indexOf("'");
            int end = lower.indexOf("'", start + 1);
            if (start != -1 && end != -1) {
                return message.substring(start + 1, end);
            }
        }
        return "Issue created via MCP-LLM";
    }
    
    private String extractSearchTerms(String message) {
        // Extract meaningful search terms from the message
        String lower = message.toLowerCase();
        if (lower.contains("about")) {
            int aboutIndex = lower.indexOf("about");
            return message.substring(aboutIndex + 5).trim();
        }
        return message;
    }
    
    private String extractPageTitle(String message) {
        // Extract page title from message
        String lower = message.toLowerCase();
        if (lower.contains("'")) {
            int start = lower.indexOf("'");
            int end = lower.indexOf("'", start + 1);
            if (start != -1 && end != -1) {
                return message.substring(start + 1, end);
            }
        }
        return "New Page";
    }
}
