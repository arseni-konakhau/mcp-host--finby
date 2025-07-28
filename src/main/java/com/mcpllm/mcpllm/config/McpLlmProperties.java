package com.mcpllm.mcpllm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.Duration;

@ConfigurationProperties(prefix = "mcp")
@Validated
public record McpLlmProperties(
    Client client,
    Intent intent,
    Security security
) {
    public record Client(
        @NotBlank String url,
        Duration timeout
    ) {}
    
    public record Intent(
        @Min(0) @Max(1) double confidenceThreshold,
        boolean mcpEnabled,
        boolean llmEnabled
    ) {}
    
    public record Security(
        String apiKey
    ) {}
}
