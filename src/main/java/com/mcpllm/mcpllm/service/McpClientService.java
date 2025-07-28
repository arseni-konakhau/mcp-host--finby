package com.mcpllm.mcpllm.service;

import com.mcpllm.mcpllm.config.McpLlmProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class McpClientService {

    private final WebClient mcpClientWebClient;
    private final McpLlmProperties properties;

    public McpClientService(@Qualifier("mcpClientWebClient") WebClient mcpClientWebClient,
                           McpLlmProperties properties) {
        this.mcpClientWebClient = mcpClientWebClient;
        this.properties = properties;
    }

    public Mono<Map<String, Object>> executeJiraAction(String action, Map<String, Object> parameters) {
        var requestBody = Map.of(
            "service", "jira",
            "action", action,
            "parameters", parameters
        );

        return mcpClientWebClient
            .post()
            .uri("/mcp/execute")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(map -> (Map<String, Object>) map)
            .timeout(properties.client().timeout())
            .onErrorResume(throwable -> {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("success", false);
                errorMap.put("error", "Failed to execute Jira action: " + throwable.getMessage());
                return Mono.just(errorMap);
            });
    }

    public Mono<Map<String, Object>> executeConfluenceAction(String action, Map<String, Object> parameters) {
        var requestBody = Map.of(
            "service", "confluence",
            "action", action,
            "parameters", parameters
        );

        return mcpClientWebClient
            .post()
            .uri("/mcp/execute")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(map -> (Map<String, Object>) map)
            .timeout(properties.client().timeout())
            .onErrorResume(throwable -> {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("success", false);
                errorMap.put("error", "Failed to execute Confluence action: " + throwable.getMessage());
                return Mono.just(errorMap);
            });
    }

    public Mono<Map<String, Object>> getAvailableTools() {
        return mcpClientWebClient
            .get()
            .uri("/mcp/tools")
            .retrieve()
            .bodyToMono(Map.class)
            .map(map -> (Map<String, Object>) map)
            .timeout(properties.client().timeout())
            .onErrorResume(throwable -> {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("success", false);
                errorMap.put("error", "Failed to get available tools: " + throwable.getMessage());
                return Mono.just(errorMap);
            });
    }
}
