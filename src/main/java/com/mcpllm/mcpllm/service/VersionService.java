package com.mcpllm.mcpllm.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VersionService {

    public Map<String, String> getVersionInfo() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("name", "mcp-llm");
        versionInfo.put("version", "0.0.1-SNAPSHOT");
        return versionInfo;
    }
}
