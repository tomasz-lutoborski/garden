(ns garden.views.pages
  "Page rendering functions."
  (:require [garden.views.layout :as layout]
            [garden.posts :as posts]
            [hiccup2.core :as h]))

(def prose-classes
  "prose-p:leading-relaxed prose-p:mb-6 prose-headings:text-accent-light dark:prose-headings:text-accent-dark prose-headings:font-mono prose-headings:tracking-tight prose-h2:text-2xl prose-h2:mt-12 prose-h2:mb-4 prose-h3:text-xl prose-h3:mt-8 prose-h3:mb-3 prose-a:text-link-light dark:prose-a:text-link-dark prose-a:underline prose-a:underline-offset-2 prose-a:decoration-1 hover:prose-a:text-hover-light dark:hover:prose-a:text-hover-dark prose-strong:text-ink-light dark:prose-strong:text-ink-dark prose-strong:font-semibold prose-code:text-accent-light dark:prose-code:text-accent-dark prose-code:font-mono prose-code:text-base prose-blockquote:border-l-accent-light dark:prose-blockquote:border-l-accent-dark prose-blockquote:border-l-2 prose-blockquote:pl-6 prose-blockquote:italic prose-blockquote:text-muted-light dark:prose-blockquote:text-muted-dark prose-ul:my-6 prose-li:my-2")

(defn stage-badge
  "Return an emoji badge for the given stage."
  [stage]
  (case stage
    "seedling" "🌱"
    "growing" "🌿"
    "evergreen" "🌳"
    "🌱"))

(defn index-page
  "Render the index page with post listing."
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/layout "garden"
                        [:h1.text-4xl.font-mono.text-accent-light.dark:text-accent-dark.mb-12.tracking-tight "garden"]
                        [:ul.space-y-6
                         (for [{:keys [slug title date stage]} (posts/public-posts)]
                           [:li.group
                            [:a.text-link-light.dark:text-link-dark.hover:text-hover-light.dark:hover:text-hover-dark.transition-colors.text-xl.leading-snug.block
                             {:href (str "/post/" slug)}
                             [:span.mr-2 {:title stage} (stage-badge stage)]
                             title]
                            [:span.text-muted-light.dark:text-muted-dark.text-base.mt-1.block date]])])})

(defn post-page
  "Render a single post page."
  [{{:keys [slug]} :path-params}]
  (if-let [post (posts/find-post slug)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (layout/layout (:title post)
                          [:article
                           [:header.mb-10
                            [:h1.text-4xl.font-mono.text-accent-light.dark:text-accent-dark.mb-4.tracking-tight.leading-tight (:title post)]
                            [:p.text-muted-light.dark:text-muted-dark.text-base
                             [:span.mr-2 {:title (:stage post)} (stage-badge (:stage post))]
                             (:date post)]]
                           [:div.prose.prose-lg.max-w-none.text-ink-light.dark:text-ink-dark
                            {:class prose-classes}
                            (h/raw (:html post))]])}
    {:status 404
     :headers {"Content-Type" "text/html"}
     :body (layout/layout "not found" [:p.text-muted-light.dark:text-muted-dark.text-xl "Post not found."])}))

(defn not-found
  "Render a 404 page."
  [_request]
  {:status 404
   :headers {"Content-Type" "text/plain"}
   :body "Not found"})
