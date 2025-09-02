# Tools



## Research

### JSON
- tools: 12381 tokens, 49525 characters, 33473 words (45KB)
    - jira: 
    - confluence: 

### YAML
- tools: 10856 tokens 43425 characters 16023 words (55 KB)
    - jira: 
    - confluence: 



## Recognition Prompt

```json
[
    {
        // we obtain list of pages where (based on description of "confluence_search" function of rules-JSON)
        "confluence_search": {
            "toDisplay:": false,
            "properties": {
                // each property has to be composed according to it's description in that JSON
                // like in following example
                "query": "text ~ \"Automatic\" and type = page",
                "limit": "",
                "spaces_filter": ""
            }
        }
    },

    {
        "confluence_get_page": {
            "toDisplay:": false,
        }
    },

    {
        "confluence_get_comments": {
            "toDisplay:": false,
        }
    }
]
```


