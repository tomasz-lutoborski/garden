# Repository Guidelines

## Project Structure & Module Organization

- `src/garden/` — application code (Ring/Jetty entrypoint, routes, views, post loader).
- `posts/` — Markdown content files (`*.md`) with optional frontmatter.
- `test/garden/` — tests (Clojure `clojure.test`).
- `deps.edn` — dependencies and aliases.
- `makefile` — common dev commands.
- `Dockerfile` — multi-stage build for `garden.jar`.

Key namespaces:
- `garden.core` — server startup + middleware wiring.
- `garden.routes` — Reitit routes.
- `garden.posts` — post parsing/loading.
- `garden.views.*` — HTML rendering (Hiccup).

## Build, Test, and Development Commands

- `make run` — run local dev server (defaults to `PORT=3000`).
- `make test` — run the test suite via `clj -X:test`.
- `make build` — build the Docker image (`garden`).

Useful environment variables:
- `PORT=4000 make run` — change port.
- `GARDEN_RELOAD=0 make run` — disable Ring reload middleware.
- `GARDEN_TIMING=1 make run` — print per-request timing to stdout.

## Coding Style & Naming Conventions

- Clojure: use 2-space indentation and idiomatic `ns` ordering (`:require` sorted/grouped).
- Prefer small, pure functions; keep I/O in `garden.posts` and HTTP wiring in `garden.core`.
- Namespaces follow `garden.<area>`; functions use kebab-case (e.g., `public-posts`).

Linting/formatting:
- `.clj-kondo/` is present; use `clj-kondo` if available to catch common issues.

## Testing Guidelines

- Framework: `clojure.test` run via Cognitect test-runner (`:test` alias in `deps.edn`).
- Place tests under `test/` and name them `*_test.clj` (e.g., `test/garden/core_test.clj`).
- Tests should be deterministic and avoid network calls; prefer `with-redefs` for I/O isolation.

## Commit & Pull Request Guidelines

- Commit messages in history are short, imperative, and descriptive (e.g., “improved load time”).
- Keep commits focused; avoid mixing refactors with behavior changes.
- PRs should include: a brief summary, how to verify (commands/URLs), and note any env vars used.

