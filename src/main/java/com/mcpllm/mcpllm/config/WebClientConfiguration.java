package com.mcpllm.mcpllm.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties({McpLlmProperties.class, DeepSeekProperties.class})
public class WebClientConfiguration {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)); // 1MB
    }

    @Bean
    public WebClient mcpClientWebClient(WebClient.Builder builder, McpLlmProperties properties) {
        return builder
            .baseUrl(properties.client().url())
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    @Bean
    public WebClient deepSeekWebClient(WebClient.Builder builder, DeepSeekProperties properties) {
        return builder
            .baseUrl(properties.apiUrl())
            .defaultHeader("Authorization", "Bearer " + properties.apiToken())
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}
