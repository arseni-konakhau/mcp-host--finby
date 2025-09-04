# MCP Host Recognition Process
MCP - Model Context Protocol

### User Input:
`Looking for confluence pages where word "Жираф" is present, and as many pages I've got I need you to return whole content of each of them. Then I need you to analyze what this creature eats. Compose output `


## Rules:
- You have to recognize MCP Communication and provide list of MCP Calloust to obtain required info
- "User Input" may include two types of information
    - Related to MCP execution
    - Regular request to LLM
- Based on "User Input" we have to define MCP callouts to MCP Client in order to bring more context via MCP communication and then with this content we are going to engage "User Input" again to LLM to pefrom over whole inforamtion regular request to LLM. So this operation is intermediate in full cycle of communication between user and LLM.
- I need you to reply in format to execute REST CLIENT calliut
- I need no additional information and explanations but requests I have to make to my MCP Client
- Follow examples below


## Output Format:
- set of taskss to MCP: http rest-client operations we have to perform in order to obtain additional information via MCP Server (using http format as presented below in example seciton)
- Set of tasks to LLM after MCP returns result to augment the promt to LLM: just list of questions to LLM that is not matching MCP communication but need for whole informatioon aanlisys


## Examples For Output:
- here is the set of examples on how I colaborate to MCP Client
- Based on fllowing examples I expect you to compose following http rest-client executions with proper order to achive resolution based on "User Input" (from above)
- Make sure you follow "Rules" while you compose output
```http
### 3.1 Confluence Search - Basic
POST {{baseUrl}}/api/mcp/tools/confluence_search/execute
Content-Type: application/json

{
  "query": "project documentation",
  "limit": 10
}

### Expected Response: Search results from Confluence

### 3.2 Confluence Search with CQL
POST {{baseUrl}}/api/mcp/tools/confluence_search/execute
Content-Type: application/json

{
  "query": "type=page AND space=DEV",
  "limit": 15
}

### Expected Response: CQL search results

### 3.3 Confluence Search User
POST {{baseUrl}}/api/mcp/tools/confluence_search_user/execute
Content-Type: application/json

{
  "query": "user.fullname ~ \"John Doe\"",
  "limit": 5
}

### Expected Response: User search results

# =============================================
# CONFLUENCE - PAGES
# =============================================

### 3.4 Get Confluence Page by ID
POST {{baseUrl}}/api/mcp/tools/confluence_get_page/execute
Content-Type: application/json

{
  "page_id": "123456789"
}

### Expected Response: Page content and metadata

### 3.5 Get Confluence Page by Title and Space
POST {{baseUrl}}/api/mcp/tools/confluence_get_page/execute
Content-Type: application/json

{
  "title": "Project Documentation",
  "space_key": "DEV"
}

### Expected Response: Page content and metadata

### 3.6 Get Page Children
POST {{baseUrl}}/api/mcp/tools/confluence_get_page_children/execute
Content-Type: application/json

{
  "parent_id": "123456789",
  "limit": 10
}

### Expected Response: List of child pages

# =============================================
# CONFLUENCE - COMMENTS
# =============================================

### 3.7 Get Page Comments
POST {{baseUrl}}/api/mcp/tools/confluence_get_comments/execute
Content-Type: application/json

{
  "page_id": "123456789"
}

### Expected Response: List of comments for the page

### 3.8 Add Comment (Write Operation - if enabled)
POST {{baseUrl}}/api/mcp/tools/confluence_add_comment/execute
Content-Type: application/json

{
  "page_id": "123456789",
  "content": "This comment was added via MCP Client for testing."
}

### Expected Response: Created comment object or error if read-only mode

# =============================================
# CONFLUENCE - LABELS
# =============================================

### 3.9 Get Page Labels
POST {{baseUrl}}/api/mcp/tools/confluence_get_labels/execute
Content-Type: application/json

{
  "page_id": "123456789"
}

### Expected Response: List of labels for the page

### 3.10 Add Label (Write Operation - if enabled)
POST {{baseUrl}}/api/mcp/tools/confluence_add_label/execute
Content-Type: application/json

{
  "page_id": "123456789",
  "name": "testing"
}

### Expected Response: Updated list of labels or error if read-only mode

# =============================================
# CONFLUENCE - PAGE MANAGEMENT (WRITE OPERATIONS)
# =============================================

### 3.11 Create Confluence Page (Write Operation - if enabled)
POST {{baseUrl}}/api/mcp/tools/confluence_create_page/execute
Content-Type: application/json

{
  "space_key": "DEV",
  "title": "Test Page via MCP Client",
  "content": "# Test Page\n\nThis page was created via MCP Client for testing.\n\n## Features Tested\n- MCP Client integration\n- Authentication\n- Page creation",
  "content_format": "markdown"
}

### Expected Response: Created page object or error if read-only mode

### 3.12 Update Confluence Page (Write Operation - if enabled)
POST {{baseUrl}}/api/mcp/tools/confluence_update_page/execute
Content-Type: application/json

{
  "page_id": "123456789",
  "title": "Updated Test Page via MCP Client",
  "content": "# Updated Test Page\n\nThis page was updated via MCP Client.\n\n## Updated Features\n- MCP Client integration\n- Authentication\n- Page updates",
  "content_format": "markdown"
}

### Expected Response: Updated page object or error if read-only mode

### 3.13 Delete Confluence Page (Write Operation - if enabled)
POST {{baseUrl}}/api/mcp/tools/confluence_delete_page/execute
Content-Type: application/json

{
  "page_id": "999999999"
}

### Expected Response: Success confirmation or error if read-only mode
```








### List of Available Tools For Confluence (using YAML)

- This is list of available tools of MCP Server for Atlassian
- Based on this information you have to compse expected result "Output" properly

```yaml
- name: confluence_search
  description: |
    Search Confluence content using simple terms or CQL.

    Args:
        ctx: The FastMCP context.
        query: Search query - can be simple text or a CQL query string.
        limit: Maximum number of results (1-50).
        spaces_filter: Comma-separated list of space keys to filter by.

    Returns:
        JSON string representing a list of simplified Confluence page objects.
  inputSchema:
    properties:
      query:
        description: >-
          Search query - can be either a simple text (e.g. 'project
          documentation') or a CQL query string. Simple queries use 'siteSearch'
          by default, to mimic the WebUI search, with an automatic fallback to
          'text' search if not supported. Examples of CQL:

          - Basic search: 'type=page AND space=DEV'

          - Personal space search: 'space="~username"' (note: personal space
          keys starting with ~ must be quoted)

          - Search by title: 'title~"Meeting Notes"'

          - Use siteSearch: 'siteSearch ~ "important concept"'

          - Use text search: 'text ~ "important concept"'

          - Recent content: 'created >= "2023-01-01"'

          - Content with specific label: 'label=documentation'

          - Recently modified content: 'lastModified > startOfMonth("-1M")'

          - Content modified this year: 'creator = currentUser() AND
          lastModified > startOfYear()'

          - Content you contributed to recently: 'contributor = currentUser()
          AND lastModified > startOfWeek()'

          - Content watched by user: 'watcher = "user@domain.com" AND type =
          page'

          - Exact phrase in content: 'text ~ "\"Urgent Review Required\"" AND
          label = "pending-approval"'

          - Title wildcards: 'title ~ "Minutes*" AND (space = "HR" OR space =
          "Marketing")'

          Note: Special identifiers need proper quoting in CQL: personal space
          keys (e.g., "~username"), reserved words, numeric IDs, and identifiers
          with special characters.
        title: Query
        type: string
      limit:
        default: 10
        description: Maximum number of results (1-50)
        maximum: 50
        minimum: 1
        title: Limit
        type: integer
      spaces_filter:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: >-
          (Optional) Comma-separated list of space keys to filter results by.
          Overrides the environment variable CONFLUENCE_SPACES_FILTER if
          provided. Use empty string to disable filtering.
        title: Spaces Filter
    required:
      - query
    type: object



- name: confluence_get_page
  description: >
    Get content of a specific Confluence page by its ID, or by its title and
    space key.


    Args:
        ctx: The FastMCP context.
        page_id: Confluence page ID. If provided, 'title' and 'space_key' are ignored.
        title: The exact title of the page. Must be used with 'space_key'.
        space_key: The key of the space. Must be used with 'title'.
        include_metadata: Whether to include page metadata.
        convert_to_markdown: Convert content to markdown (true) or keep raw HTML (false).

    Returns:
        JSON string representing the page content and/or metadata, or an error if not found or parameters are invalid.
  inputSchema:
    properties:
      page_id:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: >-
          Confluence page ID (numeric ID, can be found in the page URL). For
          example, in the URL
          'https://example.atlassian.net/wiki/spaces/TEAM/pages/123456789/Page+Title',
          the page ID is '123456789'. Provide this OR both 'title' and
          'space_key'. If page_id is provided, title and space_key will be
          ignored.
        title: Page Id
      title:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: >-
          The exact title of the Confluence page. Use this with 'space_key' if
          'page_id' is not known.
        title: Title
      space_key:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: >-
          The key of the Confluence space where the page resides (e.g., 'DEV',
          'TEAM'). Required if using 'title'.
        title: Space Key
      include_metadata:
        default: true
        description: >-
          Whether to include page metadata such as creation date, last update,
          version, and labels.
        title: Include Metadata
        type: boolean
      convert_to_markdown:
        default: true
        description: >-
          Whether to convert page to markdown (true) or keep it in raw HTML
          format (false). Raw HTML can reveal macros (like dates) not visible in
          markdown, but CAUTION: using HTML significantly increases token usage
          in AI responses.
        title: Convert To Markdown
        type: boolean
    type: object



- name: confluence_get_page_children
  description: |
    Get child pages of a specific Confluence page.

    Args:
        ctx: The FastMCP context.
        parent_id: The ID of the parent page.
        expand: Fields to expand.
        limit: Maximum number of child pages.
        include_content: Whether to include page content.
        convert_to_markdown: Convert content to markdown if include_content is true.
        start: Starting index for pagination.

    Returns:
        JSON string representing a list of child page objects.
  inputSchema:
    properties:
      parent_id:
        description: The ID of the parent page whose children you want to retrieve
        title: Parent Id
        type: string
      expand:
        default: version
        description: 'Fields to expand in the response (e.g., ''version'', ''body.storage'')'
        title: Expand
        type: string
      limit:
        default: 25
        description: Maximum number of child pages to return (1-50)
        maximum: 50
        minimum: 1
        title: Limit
        type: integer
      include_content:
        default: false
        description: Whether to include the page content in the response
        title: Include Content
        type: boolean
      convert_to_markdown:
        default: true
        description: >-
          Whether to convert page content to markdown (true) or keep it in raw
          HTML format (false). Only relevant if include_content is true.
        title: Convert To Markdown
        type: boolean
      start:
        default: 0
        description: Starting index for pagination (0-based)
        minimum: 0
        title: Start
        type: integer
    required:
      - parent_id
    type: object



- name: confluence_get_comments
  description: |
    Get comments for a specific Confluence page.

    Args:
        ctx: The FastMCP context.
        page_id: Confluence page ID.

    Returns:
        JSON string representing a list of comment objects.
  inputSchema:
    properties:
      page_id:
        description: >-
          Confluence page ID (numeric ID, can be parsed from URL, e.g. from
          'https://example.atlassian.net/wiki/spaces/TEAM/pages/123456789/Page+Title'
          -> '123456789')
        title: Page Id
        type: string
    required:
      - page_id
    type: object



- name: confluence_get_labels
  description: |
    Get labels for a specific Confluence page.

    Args:
        ctx: The FastMCP context.
        page_id: Confluence page ID.

    Returns:
        JSON string representing a list of label objects.
  inputSchema:
    properties:
      page_id:
        description: >-
          Confluence page ID (numeric ID, can be parsed from URL, e.g. from
          'https://example.atlassian.net/wiki/spaces/TEAM/pages/123456789/Page+Title'
          -> '123456789')
        title: Page Id
        type: string
    required:
      - page_id
    type: object



- name: confluence_add_label
  description: |
    Add label to an existing Confluence page.

    Args:
        ctx: The FastMCP context.
        page_id: The ID of the page to update.
        name: The name of the label.

    Returns:
        JSON string representing the updated list of label objects for the page.

    Raises:
        ValueError: If in read-only mode or Confluence client is unavailable.
  inputSchema:
    properties:
      page_id:
        description: The ID of the page to update
        title: Page Id
        type: string
      name:
        description: The name of the label
        title: Name
        type: string
    required:
      - page_id
      - name
    type: object



- name: confluence_create_page
  description: |
    Create a new Confluence page.

    Args:
        ctx: The FastMCP context.
        space_key: The key of the space.
        title: The title of the page.
        content: The content of the page (format depends on content_format).
        parent_id: Optional parent page ID.
        content_format: The format of the content ('markdown', 'wiki', or 'storage').
        enable_heading_anchors: Whether to enable heading anchors (markdown only).

    Returns:
        JSON string representing the created page object.

    Raises:
        ValueError: If in read-only mode, Confluence client is unavailable, or invalid content_format.
  inputSchema:
    properties:
      space_key:
        description: >-
          The key of the space to create the page in (usually a short uppercase
          code like 'DEV', 'TEAM', or 'DOC')
        title: Space Key
        type: string
      title:
        description: The title of the page
        title: Title
        type: string
      content:
        description: >-
          The content of the page. Format depends on content_format parameter.
          Can be Markdown (default), wiki markup, or storage format
        title: Content
        type: string
      parent_id:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: >-
          (Optional) parent page ID. If provided, this page will be created as a
          child of the specified page
        title: Parent Id
      content_format:
        default: markdown
        description: >-
          (Optional) The format of the content parameter. Options: 'markdown'
          (default), 'wiki', or 'storage'. Wiki format uses Confluence wiki
          markup syntax
        title: Content Format
        type: string
      enable_heading_anchors:
        default: false
        description: >-
          (Optional) Whether to enable automatic heading anchor generation. Only
          applies when content_format is 'markdown'
        title: Enable Heading Anchors
        type: boolean
    required:
      - space_key
      - title
      - content
    type: object



- name: confluence_update_page
  description: |
    Update an existing Confluence page.

    Args:
        ctx: The FastMCP context.
        page_id: The ID of the page to update.
        title: The new title of the page.
        content: The new content of the page (format depends on content_format).
        is_minor_edit: Whether this is a minor edit.
        version_comment: Optional comment for this version.
        parent_id: Optional new parent page ID.
        content_format: The format of the content ('markdown', 'wiki', or 'storage').
        enable_heading_anchors: Whether to enable heading anchors (markdown only).

    Returns:
        JSON string representing the updated page object.

    Raises:
        ValueError: If Confluence client is not configured, available, or invalid content_format.
  inputSchema:
    properties:
      page_id:
        description: The ID of the page to update
        title: Page Id
        type: string
      title:
        description: The new title of the page
        title: Title
        type: string
      content:
        description: >-
          The new content of the page. Format depends on content_format
          parameter
        title: Content
        type: string
      is_minor_edit:
        default: false
        description: Whether this is a minor edit
        title: Is Minor Edit
        type: boolean
      version_comment:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: Optional comment for this version
        title: Version Comment
      parent_id:
        anyOf:
          - type: string
          - type: 'null'
        default: null
        description: Optional the new parent page ID
        title: Parent Id
      content_format:
        default: markdown
        description: >-
          (Optional) The format of the content parameter. Options: 'markdown'
          (default), 'wiki', or 'storage'. Wiki format uses Confluence wiki
          markup syntax
        title: Content Format
        type: string
      enable_heading_anchors:
        default: false
        description: >-
          (Optional) Whether to enable automatic heading anchor generation. Only
          applies when content_format is 'markdown'
        title: Enable Heading Anchors
        type: boolean
    required:
      - page_id
      - title
      - content
    type: object



- name: confluence_delete_page
  description: |
    Delete an existing Confluence page.

    Args:
        ctx: The FastMCP context.
        page_id: The ID of the page to delete.

    Returns:
        JSON string indicating success or failure.

    Raises:
        ValueError: If Confluence client is not configured or available.
  inputSchema:
    properties:
      page_id:
        description: The ID of the page to delete
        title: Page Id
        type: string
    required:
      - page_id
    type: object



- name: confluence_add_comment
  description: |
    Add a comment to a Confluence page.

    Args:
        ctx: The FastMCP context.
        page_id: The ID of the page to add a comment to.
        content: The comment content in Markdown format.

    Returns:
        JSON string representing the created comment.

    Raises:
        ValueError: If in read-only mode or Confluence client is unavailable.
  inputSchema:
    properties:
      page_id:
        description: The ID of the page to add a comment to
        title: Page Id
        type: string
      content:
        description: The comment content in Markdown format
        title: Content
        type: string
    required:
      - page_id
      - content
    type: object



- name: confluence_search_user
  description: |
    Search Confluence users using CQL.

    Args:
        ctx: The FastMCP context.
        query: Search query - a CQL query string for user search.
        limit: Maximum number of results (1-50).

    Returns:
        JSON string representing a list of simplified Confluence user search result objects.
  inputSchema:
    properties:
      query:
        description: >-
          Search query - a CQL query string for user search. Examples of CQL:

          - Basic user lookup by full name: 'user.fullname ~ "First Last"'

          Note: Special identifiers need proper quoting in CQL: personal space
          keys (e.g., "~username"), reserved words, numeric IDs, and identifiers
          with special characters.
        title: Query
        type: string
      limit:
        default: 10
        description: Maximum number of results (1-50)
        maximum: 50
        minimum: 1
        title: Limit
        type: integer
    required:
      - query
    type: object

```