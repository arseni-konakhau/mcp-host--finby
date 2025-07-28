package com.mcpllm.mcpllm.model;

public record IntentAnalysisResult(
    IntentType type,
    double confidence,
    String service
) {}
