# Getting Started

1. Run with Spring Configuration to debug startup (bean injection, etc.). 
2. An MCP client (or _npx inspector_) must be running the jar in order for the debugger to attach.
3. Flags (-D)/env(capital snake case) are `jira.server.url`, `jira.server.username`, `jira.server.password`.

## Known Issues
1. Still can't log to file due to docker volume mount issues.