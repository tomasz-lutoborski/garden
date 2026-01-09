(ns garden.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.reload :refer [wrap-reload]]
            [reitit.ring :as ring]
            [hiccup2.core :as h]
            [markdown.core :as md]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:gen-class))

(def posts-dir "posts")

(defn parse-frontmatter [content]
  (let [[_ frontmatter body] (re-matches #"(?s)---\n(.+?)\n---\n(.+)" content)]
    (if frontmatter
      {:meta (->> (str/split-lines frontmatter)
                  (map #(str/split % #":\s*" 2))
                  (filter #(= 2 (count %)))
                  (into {}))
       :body body}
      {:meta {} :body content})))

(defn load-post [file]
  (let [filename (.getName file)
        slug (str/replace filename #"\.md$" "")
        content (slurp file)
        {:keys [meta body]} (parse-frontmatter content)]
    {:slug slug
     :title (get meta "title" slug)
     :date (get meta "date" (subs slug 0 10))
     :body body
     :html (md/md-to-html-string body)}))

(defn load-posts []
  (->> (file-seq (io/file posts-dir))
       (filter #(.isFile %))
       (filter #(str/ends-with? (.getName %) ".md"))
       (map load-post)
       (sort-by :date)
       reverse))

(defn layout [title & body]
  (str
   (h/html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:title title]
      [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
      [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
      [:link {:href "https://fonts.googleapis.com/css2?family=Raleway:wght@400;500;600&display=swap" :rel "stylesheet"}]
      [:link {:href "https://cdn.jsdelivr.net/npm/geist@1.3.1/dist/fonts/geist-mono/style.min.css" :rel "stylesheet"}]
      [:script {:src "https://cdn.tailwindcss.com?plugins=typography"}]
      [:script (h/raw "
        tailwind.config = {
          darkMode: 'class',
          theme: {
            extend: {
              fontFamily: {
                mono: ['Geist Mono', 'monospace'],
                body: ['Raleway', 'sans-serif'],
              },
              colors: {
                surface: {
                  light: '#faf6f3',
                  dark: '#1a1520',
                },
                ink: {
                  light: '#4a4544',
                  dark: '#e5e0db',
                },
                muted: {
                  light: '#9a918c',
                  dark: '#7a7080',
                },
                accent: {
                  light: '#c27878',
                  dark: '#e8a5a5',
                },
                link: {
                  light: '#c48c6c',
                  dark: '#e8b899',
                },
                hover: {
                  light: '#a86060',
                  dark: '#f0c0b0',
                },
              }
            }
          }
        }
      ")]
      [:script (h/raw "
        if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
          document.documentElement.classList.add('dark')
        } else {
          document.documentElement.classList.remove('dark')
        }
      ")]]
     [:body.min-h-screen.font-body.bg-surface-light.text-ink-light.dark:bg-surface-dark.dark:text-ink-dark.transition-colors.duration-200.text-lg.leading-relaxed.antialiased
      [:div.max-w-2xl.mx-auto.px-6.py-12.sm:py-16
       [:nav.mb-16.flex.justify-between.items-center
        [:a.text-link-light.dark:text-link-dark.hover:text-hover-light.dark:hover:text-hover-dark.transition-colors.font-mono.text-base.tracking-wide {:href "/"} "home"]
        [:button#theme-toggle.font-mono.text-sm.tracking-wide.px-4.py-1.5.rounded-full.border.border-muted-light.dark:border-muted-dark.text-muted-light.dark:text-muted-dark.hover:text-accent-light.dark:hover:text-accent-dark.hover:border-accent-light.dark:hover:border-accent-dark.transition-colors
         {:onclick "toggleTheme()"} "theme"]]
       body]
      [:script (h/raw "
        function toggleTheme() {
          if (document.documentElement.classList.contains('dark')) {
            document.documentElement.classList.remove('dark');
            localStorage.theme = 'light';
          } else {
            document.documentElement.classList.add('dark');
            localStorage.theme = 'dark';
          }
        }
      ")]]])))

(defn index-page [_]
  (let [posts (load-posts)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (layout "garden"
                   [:h1.text-4xl.font-mono.text-accent-light.dark:text-accent-dark.mb-12.tracking-tight "garden"]
                   [:ul.space-y-6
                    (for [{:keys [slug title date]} posts]
                      [:li.group
                       [:a.text-link-light.dark:text-link-dark.hover:text-hover-light.dark:hover:text-hover-dark.transition-colors.text-xl.leading-snug.block
                        {:href (str "/post/" slug)} title]
                       [:span.text-muted-light.dark:text-muted-dark.text-base.mt-1.block date]])])}))

(defn post-page [{{:keys [slug]} :path-params}]
  (let [posts (load-posts)
        post (first (filter #(= slug (:slug %)) posts))]
    (if post
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (layout (:title post)
                     [:article
                      [:header.mb-10
                       [:h1.text-4xl.font-mono.text-accent-light.dark:text-accent-dark.mb-4.tracking-tight.leading-tight (:title post)]
                       [:p.text-muted-light.dark:text-muted-dark.text-base (:date post)]]
                      [:div.prose.prose-lg.max-w-none.text-ink-light.dark:text-ink-dark
                       {:class "prose-p:leading-relaxed prose-p:mb-6 prose-headings:text-accent-light dark:prose-headings:text-accent-dark prose-headings:font-mono prose-headings:tracking-tight prose-h2:text-2xl prose-h2:mt-12 prose-h2:mb-4 prose-h3:text-xl prose-h3:mt-8 prose-h3:mb-3 prose-a:text-link-light dark:prose-a:text-link-dark prose-a:underline prose-a:underline-offset-2 prose-a:decoration-1 hover:prose-a:text-hover-light dark:hover:prose-a:text-hover-dark prose-strong:text-ink-light dark:prose-strong:text-ink-dark prose-strong:font-semibold prose-code:text-accent-light dark:prose-code:text-accent-dark prose-code:font-mono prose-code:text-base prose-blockquote:border-l-accent-light dark:prose-blockquote:border-l-accent-dark prose-blockquote:border-l-2 prose-blockquote:pl-6 prose-blockquote:italic prose-blockquote:text-muted-light dark:prose-blockquote:text-muted-dark prose-ul:my-6 prose-li:my-2"}
                       (h/raw (:html post))]])}
      {:status 404
       :headers {"Content-Type" "text/html"}
       :body (layout "not found" [:p.text-muted-light.dark:text-muted-dark.text-xl "Post not found."])})))

(defn not-found [_]
  {:status 404
   :headers {"Content-Type" "text/plain"}
   :body "Not found"})

(def app
  (-> (ring/ring-handler
       (ring/router
        [["/" {:get #'index-page}]
         ["/post/:slug" {:get #'post-page}]])
       #'not-found)
      (wrap-reload)))

(defn -main [& _]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Running on http://localhost:" port))
    (jetty/run-jetty app {:port port :join? true})))