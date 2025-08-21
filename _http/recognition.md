# Recognition



```json
[
    {
        // we obtain list of pages where (based on description of "confluence_search" function of rules-JSON)
        "confluence_search": {
            "toDisplay:": false,
            "properties": {
                // each property has to be composed according to it's description in that JSON
                // like in following example
                "query": "",
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


