package com.mcpllm.mcpllm.model;

import java.util.Map;

public record ChatResponse(
    String response,
    IntentAnalysisResult intent,
    Map<String, Object> mcpResult,
    boolean success,
    String error
) {}
