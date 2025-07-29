package com.mcpllm.mcpllm.service;

import com.mcpllm.mcpllm.config.DeepSeekProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class DeepSeekService {

    private final WebClient deepSeekWebClient;
    private final DeepSeekProperties properties;

    public DeepSeekService(@Qualifier("deepSeekWebClient") WebClient deepSeekWebClient, 
                        DeepSeekProperties properties) {
        this.deepSeekWebClient = deepSeekWebClient;
        this.properties = properties;
    }

    public Mono<String> generateResponse(String userMessage, String systemPrompt) {
        var requestBody = Map.of(
            "model", "deepseek-chat",
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
            ),
            "max_tokens", 1000,
            "temperature", 0.7
        );

        return deepSeekWebClient
            .post()
            .uri("/v1/chat/completions")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(this::extractResponse)
            .timeout(properties.timeout())
            .onErrorResume(throwable -> {
                return Mono.just("I apologize, but I'm unable to process your request at the moment. Please try again later.");
            });
    }

    public Mono<String> generateMcpAnalysis(String userMessage) {
        String systemPrompt = """
            You are an AI assistant that analyzes user messages to determine if they require MCP (Model Context Protocol) actions.
            
            Analyze the user's message and determine:
            1. If it's related to Jira (issues, tickets, projects, sprints, etc.)
            2. If it's related to Confluence (pages, spaces, documents, etc.)
            3. If it requires only a conversational response
            
            Respond with a JSON object containing:
            - "intent": "jira", "confluence", or "llm_only"
            - "confidence": a number between 0 and 1
            - "reasoning": brief explanation of your decision
            
            Examples:
            - "Create a new Jira issue" -> {"intent": "jira", "confidence": 0.95, "reasoning": "Clear request to create Jira issue"}
            - "What's the weather like?" -> {"intent": "llm_only", "confidence": 0.9, "reasoning": "General question not related to Atlassian tools"}
            """;

        return generateResponse(userMessage, systemPrompt);
    }

    @SuppressWarnings("unchecked")
    private String extractResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Log error in production
        }
        return "I apologize, but I couldn't generate a proper response.";
    }
}
