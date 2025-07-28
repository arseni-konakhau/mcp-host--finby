package com.mcpllm.mcpllm.controller;

import com.mcpllm.mcpllm.model.ChatRequest;
import com.mcpllm.mcpllm.model.ChatResponse;
import com.mcpllm.mcpllm.service.ChatOrchestrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // For MVP - restrict in production
public class ChatController {

    private final ChatOrchestrationService chatOrchestrationService;

    public ChatController(ChatOrchestrationService chatOrchestrationService) {
        this.chatOrchestrationService = chatOrchestrationService;
    }

    @PostMapping("/message")
    public Mono<ResponseEntity<ChatResponse>> processMessage(@Valid @RequestBody ChatRequest request) {
        return chatOrchestrationService.processMessage(request)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("MCP-LLM Service is running"));
    }
}
