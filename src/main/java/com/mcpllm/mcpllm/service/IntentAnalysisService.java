package com.mcpllm.mcpllm.service;

import com.mcpllm.mcpllm.model.IntentAnalysisResult;
import com.mcpllm.mcpllm.model.IntentType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Service
public class IntentAnalysisService {

    // Patterns for intent detection
    private static final Pattern JIRA_PATTERN = Pattern.compile(
        "(?i).*(jira|issue|ticket|bug|story|epic|sprint|board|project|assignee|status|transition|worklog|comment).*"
    );
    
    private static final Pattern CONFLUENCE_PATTERN = Pattern.compile(
        "(?i).*(confluence|page|space|document|wiki|content|search|create page|update page|comment).*"
    );

    public Mono<IntentAnalysisResult> analyzeIntent(String message) {
        return Mono.fromCallable(() -> {
            String normalizedMessage = message.toLowerCase().trim();
            
            // Check for Jira-related keywords
            if (JIRA_PATTERN.matcher(normalizedMessage).matches()) {
                double confidence = calculateJiraConfidence(normalizedMessage);
                return new IntentAnalysisResult(IntentType.MCP, confidence, "jira");
            }
            
            // Check for Confluence-related keywords
            if (CONFLUENCE_PATTERN.matcher(normalizedMessage).matches()) {
                double confidence = calculateConfluenceConfidence(normalizedMessage);
                return new IntentAnalysisResult(IntentType.MCP, confidence, "confluence");
            }
            
            // Default to LLM-only for general queries
            return new IntentAnalysisResult(IntentType.LLM, 0.9, "llm");
        });
    }

    private double calculateJiraConfidence(String message) {
        int score = 0;
        String[] jiraKeywords = {"jira", "issue", "ticket", "bug", "story", "epic", "sprint", "board"};
        
        for (String keyword : jiraKeywords) {
            if (message.contains(keyword)) {
                score++;
            }
        }
        
        // Base confidence + keyword bonus
        return Math.min(0.7 + (score * 0.05), 0.95);
    }

    private double calculateConfluenceConfidence(String message) {
        int score = 0;
        String[] confluenceKeywords = {"confluence", "page", "space", "document", "wiki", "content"};
        
        for (String keyword : confluenceKeywords) {
            if (message.contains(keyword)) {
                score++;
            }
        }
        
        // Base confidence + keyword bonus
        return Math.min(0.7 + (score * 0.05), 0.95);
    }
}
