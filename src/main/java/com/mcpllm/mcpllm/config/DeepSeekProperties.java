package com.mcpllm.mcpllm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import java.time.Duration;

@ConfigurationProperties(prefix = "deepseek")
@Validated
public record DeepSeekProperties(
    @NotBlank String apiUrl,
    @NotBlank String apiToken,
    Duration timeout
) {}
