package com.mcpllm.mcpllm.model;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record ChatRequest(
    @NotBlank String message,
    Map<String, Object> context,
    String userId
) {}
