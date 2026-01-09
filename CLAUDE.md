# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run Commands

```bash
make run    # Run dev server locally (port 3000, or PORT env var)
make test   # Run tests
make build  # Build Docker image
```

## Architecture

This is a minimal Clojure blog/digital garden application serving markdown posts as HTML.

**Stack:**
- Ring + Jetty for HTTP server
- Reitit for routing
- Hiccup for HTML templating
- markdown-clj for Markdown parsing

**Structure:**
- `src/garden/core.clj` - Single-file application containing all logic
- `posts/` - Markdown files with optional YAML frontmatter (title, date)
- `test/garden/core_test.clj` - Tests

**Routes:**
- `/` - Index page listing all posts
- `/post/:slug` - Individual post page (slug = filename without .md)

**Post Format:**
```markdown
---
title: Post Title
date: 2025-01-01
---
Content here...
```
Posts without frontmatter use the filename as title and extract date from filename prefix (YYYY-MM-DD).

**Docker:**
- Multi-stage build: deps cached, then uberjar built, runs on Alpine JRE
- Exposes port 3000
