(ns garden.posts
  "Post loading and parsing from markdown files."
  (:require [markdown.core :as md]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def posts-dir "posts")

(defn parse-frontmatter
  "Parse YAML frontmatter from markdown content.
   Returns {:meta {key value} :body \"content\"}."
  [content]
  (let [[_ frontmatter body] (re-matches #"(?s)---\n(.+?)\n---\n(.+)" content)]
    (if frontmatter
      {:meta (->> (str/split-lines frontmatter)
                  (map #(str/split % #":\s*" 2))
                  (filter #(= 2 (count %)))
                  (into {}))
       :body body}
      {:meta {} :body content})))

(defn load-post
  "Load a single post from a markdown file."
  [file]
  (let [filename (.getName file)
        slug (str/replace filename #"\.md$" "")
        content (slurp file)
        {:keys [meta body]} (parse-frontmatter content)]
    {:slug slug
     :title (get meta "title" slug)
     :date (get meta "date" (subs slug 0 10))
     :body body
     :html (md/md-to-html-string body)}))

(defn load-posts
  "Load all posts from the posts directory, sorted by date descending."
  []
  (->> (file-seq (io/file posts-dir))
       (filter #(.isFile %))
       (filter #(str/ends-with? (.getName %) ".md"))
       (map load-post)
       (sort-by :date)
       reverse))

(defn find-post
  "Find a post by slug."
  [slug]
  (first (filter #(= slug (:slug %)) (load-posts))))
