package com.mcpllm.mcpllm.service;

import com.mcpllm.mcpllm.config.McpLlmProperties;
import com.mcpllm.mcpllm.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
            case MCP_JIRA -> processJiraRequest(request, intent);
            case MCP_CONFLUENCE -> processConfluenceRequest(request, intent);
            case HYBRID -> processHybridRequest(request, intent);
            default -> processWithLlmOnly(request, intent);
        };
    }

    private Mono<ChatResponse> processJiraRequest(ChatRequest request, IntentAnalysisResult intent) {
        // For MVP, we'll use a simple action mapping
        // In production, this would use LLM to extract parameters
        String action = extractJiraAction(request.message());
        Map<String, Object> parameters = extractJiraParameters(request.message());

        return mcpClientService.executeJiraAction(action, parameters)
            .flatMap(mcpResult -> {
                if ((Boolean) mcpResult.getOrDefault("success", false)) {
                    return generateContextualResponse(request.message(), mcpResult, "jira")
                        .map(response -> new ChatResponse(response, intent, mcpResult, true, null));
                } else {
                    return processWithLlmOnly(request, intent);
                }
            });
    }

    private Mono<ChatResponse> processConfluenceRequest(ChatRequest request, IntentAnalysisResult intent) {
        String action = extractConfluenceAction(request.message());
        Map<String, Object> parameters = extractConfluenceParameters(request.message());

        return mcpClientService.executeConfluenceAction(action, parameters)
            .flatMap(mcpResult -> {
                if ((Boolean) mcpResult.getOrDefault("success", false)) {
                    return generateContextualResponse(request.message(), mcpResult, "confluence")
                        .map(response -> new ChatResponse(response, intent, mcpResult, true, null));
                } else {
                    return processWithLlmOnly(request, intent);
                }
            });
    }

    private Mono<ChatResponse> processHybridRequest(ChatRequest request, IntentAnalysisResult intent) {
        // For MVP, treat hybrid as LLM-only
        return processWithLlmOnly(request, intent);
    }

    private Mono<ChatResponse> processWithLlmOnly(ChatRequest request, IntentAnalysisResult intent) {
        String systemPrompt = "You are a helpful AI assistant. Provide clear, concise, and helpful responses.";
        
        return deepSeekService.generateResponse(request.message(), systemPrompt)
            .map(response -> new ChatResponse(response, intent, null, true, null));
    }

    private Mono<String> generateContextualResponse(String userMessage, Map<String, Object> mcpResult, String service) {
        String systemPrompt = String.format("""
            You are an AI assistant that helps users understand the results of %s operations.
            
            The user asked: "%s"
            
            The operation result was: %s
            
            Provide a clear, human-friendly explanation of what happened and the results.
            Be concise but informative.
            """, service, userMessage, mcpResult.toString());

        return deepSeekService.generateResponse("Explain the result", systemPrompt);
    }

    // Simple action extraction for MVP
    private String extractJiraAction(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("create") || lower.contains("new")) return "create_issue";
        if (lower.contains("search") || lower.contains("find")) return "search_issues";
        if (lower.contains("update") || lower.contains("edit")) return "update_issue";
        return "search_issues"; // default
    }

    private String extractConfluenceAction(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("create") || lower.contains("new")) return "create_page";
        if (lower.contains("search") || lower.contains("find")) return "search_content";
        if (lower.contains("update") || lower.contains("edit")) return "update_page";
        return "search_content"; // default
    }

    private Map<String, Object> extractJiraParameters(String message) {
        // For MVP, return minimal parameters
        // In production, use LLM to extract structured parameters
        return Map.of("query", message);
    }

    private Map<String, Object> extractConfluenceParameters(String message) {
        // For MVP, return minimal parameters
        return Map.of("query", message);
    }
}
