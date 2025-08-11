# Use: install `just`, then:
#   just setup        # once
#   just shadow       # watch app + portfolio
#   just app          # watch only app
#   just portfolio    # watch only portfolio
#   just open-app     # open http://localhost:8080/
#   just open-portfolio
#   just release      # optimized build
#   just clean        # remove build artifacts

# --- constants ---------------------------------------------------------------

port := "8080"

# --- helpers ----------------------------------------------------------------

_open URL:
    # cross-platform "open in browser"
    if command -v xdg-open >/dev/null 2>&1; then xdg-open {{URL}}; \
    elif command -v open >/dev/null 2>&1; then open {{URL}}; \
    elif command -v start >/dev/null 2>&1; then start {{URL}}; \
    else echo "{{URL}}"; fi

# --- tasks ------------------------------------------------------------------

node_modules:
    npm install

shadow: node_modules
    # Watch both builds and serve at http://localhost:{{port}}/
    npx shadow-cljs -A:dev watch app-dev portfolio

tailwind: node_modules
	npx @tailwindcss/cli -i ./src/main.css -o ./resources/public/styles.css --watch

app:
    npx shadow-cljs -A:dev watch app-dev

portfolio:
    npx shadow-cljs watch portfolio

open-app:
    just _open "http://localhost:{{port}}/"

open-portfolio:
    just _open "http://localhost:{{port}}/portfolio.html"

release:
    # Production bundles for both builds
    npx shadow-cljs release app portfolio

clean:
    rm -rf .shadow-cljs resources/public/app-js resources/public/portfolio-js
