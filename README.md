# MCP Host (MCP-LLM Bridge Service / ChatBot)


### Build and Run
```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# RUN Dev
./gradlew bootRun --continuous

# With custom properties
DEEPSEEK_API_TOKEN=your_token ./gradlew bootRun
```


### macOS
```sh
$ sudo lsof -i -P -n | grep LISTEN
```

