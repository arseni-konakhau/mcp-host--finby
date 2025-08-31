package com.mcpllm.mcpllm.controller;

import com.mcpllm.mcpllm.service.VersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ChatController {

    private final VersionService versionService;

    public ChatController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping("/")
    public Mono<ResponseEntity<Map<String, String>>> root() {
        return Mono.just(ResponseEntity.ok(versionService.getVersionInfo()));
    }

    @GetMapping("/version")
    public Mono<ResponseEntity<Map<String, String>>> version() {
        return Mono.just(ResponseEntity.ok(versionService.getVersionInfo()));
    }
}
