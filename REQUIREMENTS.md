# REQUIRMENTS


## LLM:
the source of access to LLM `_http_/deepseek.http`. Here is the REST Client entries to test available LLM in order to proceed in required implementation


## Prompt Composition

### Tools
#### All Tools
- _http_/mcp-tools.yaml
- _http_/mcp-tools.json

#### Confluence
_http_/mcp-tools-confluence.yaml
_http_/mcp-tools-confluence.json

#### Jira
_http_/mcp-tools-jira.yaml
_http_/mcp-tools-jira.json



### Token Size
#### JSON
- tools: 12381 tokens, 49525 characters, 33473 words (45 KB)
    - jira: 8890 tokens 35563 characters 25300 words (32 KB)
    - confluence: 3491 tokens 13964 characters 8174 words (21 KB)

#### YAML
- tools: 10856 tokens 43425 characters 16023 words (55 KB)
    - jira: 7753 tokens 31012 characters 11513 words (40 KB)
    - confluence: 3103 tokens 12412 characters 4511 words (16 KB)

