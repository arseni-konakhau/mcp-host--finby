package com.mcpllm.mcpllm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class McpLlmApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpLlmApplication.class, args);
    }

}
