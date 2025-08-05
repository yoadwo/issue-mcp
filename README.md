# Getting Started

1. Run with Spring Configuration to debug startup (bean injection, etc.). 
2. An MCP client (or _npx inspector_) must be running the jar in order for the debugger to attach.
3. Flags (-D)/env(capital snake case) are `jira.server.url`, `jira.server.username`, `jira.server.password`.

## Dev Env
1. Use docker to pull image `mockserver/mockserver:5.15.0`
2. Run devenv.sh, which will start the container on port 55000 and set up the mock response ("expectation")
3. Configure the MCP with environments:
   1. JIRA_SERVER_URL=http://docker.for.mac.host.internal:55000
   2. JIRA_SERVER_USERNAME=user
   3. JIRA_SERVER_PASSWORD=abcd
4. remove the mock instance by running takedown.sh


## Known Issues
1. Still can't log to file due to docker volume mount issues.