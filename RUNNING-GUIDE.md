# HOW TO:

## Foreground:

### Step 1: Build the JAR
```bash
./gradlew clean build
```

### Step 2: Create .env file
Create a `.env` file in your project root:
```bash
DEEPSEEK_API_TOKEN=my-ds-token
MCP_CLIENT_URL=http://localhost:3335
SERVER_PORT=3336
```

### Step 3: Run the JAR
```bash
# Load environment variables and run
source .env && java -jar build/libs/mcp-llm-0.0.1-SNAPSHOT.jar
```

### Step 4: Test
```bash
# Health check
curl http://localhost:3336/actuator/health

# Test chat endpoint
curl -X POST http://localhost:3336/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello", "userId": "test"}'
```

That's it! Your app will be running on port 3336. The `DEEPSEEK_API_TOKEN` is kept just to make the app compile and run in its current state.



---------------

## Background


### Option 1: Using `nohup` (Recommended)
```bash
# Load env and run in background
source .env && nohup java -jar build/libs/mcp-llm-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Check if it's running
ps aux | grep java

# View logs
tail -f app.log
```


### Option 2: Using `screen`
```bash
# Start a new screen session
screen -S mcp-llm

# Load env and run
source .env && java -jar build/libs/mcp-llm-0.0.1-SNAPSHOT.jar

# Detach from screen: Press Ctrl+A, then D
# Reattach later: screen -r mcp-llm
```

### Option 3: Using `tmux`
```bash
# Start a new tmux session
tmux new-session -d -s mcp-llm

# Run the app
tmux send-keys -t mcp-llm "source .env && java -jar build/libs/mcp-llm-0.0.1-SNAPSHOT.jar" Enter

# Attach to session: tmux attach -t mcp-llm
# Detach: Press Ctrl+B, then D
```

### Stop the Background Process
```bash
# Find the process ID
ps aux | grep java

# Kill it
kill <process_id>

# Or if using nohup, kill by port
lsof -ti:3336 | xargs kill
```

**I recommend Option 1 (`nohup`) as it's the simplest and most reliable for background execution.**