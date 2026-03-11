---
description: Create a conventional commit with a clear message
allowed-tools: Bash(git add:*), Bash(git commit:*)
argument-hint: [optional message]
model: haiku
---

Stage all changed files and create a Conventional Commits message.
Format: <type>(<scope>): <short description>

If $ARGUMENTS is provided, use it as the base to generate the commit message body.
Otherwise, infer the type and scope from the staged diff.
