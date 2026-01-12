(ns garden.posts
  "Post loading and parsing from markdown files."
  (:require [markdown.core :as md]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def posts-dir "posts")

(defonce ^:private posts-cache
  (atom {:snapshot nil
         :posts []
         :by-slug {}}))

(defn clear-cache!
  "Clear the in-memory posts cache. Useful in tests or when debugging."
  []
  (reset! posts-cache {:snapshot nil :posts [] :by-slug {}}))

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
     :stage (get meta "stage" "seedling")
     :visibility (get meta "visibility" "public")
     :body body
     :html (md/md-to-html-string body)}))

(defn- md-files
  []
  (let [root (io/file posts-dir)]
    (if-not (.exists root)
      []
      (->> (file-seq root)
           (filter #(.isFile ^java.io.File %))
           (filter #(str/ends-with? (.getName ^java.io.File %) ".md"))))))

(defn- files-snapshot
  [files]
  (->> files
       (map (fn [^java.io.File f]
              [(.getPath f) (.lastModified f) (.length f)]))
       (sort-by first)
       vec))

(defn- load-posts-from-files
  [files]
  (->> files
       (map load-post)
       (sort-by :date)
       reverse
       vec))

(defn- ensure-cache!
  []
  (let [files (md-files)
        snapshot (files-snapshot files)]
    (if (= snapshot (:snapshot @posts-cache))
      @posts-cache
      (let [posts (load-posts-from-files files)
            by-slug (into {} (map (juxt :slug identity)) posts)
            new-cache {:snapshot snapshot :posts posts :by-slug by-slug}]
        (reset! posts-cache new-cache)
        new-cache))))

(defn load-posts
  "Load all posts from the posts directory, sorted by date descending."
  []
  (:posts (ensure-cache!)))

(defn public-posts
  "Load only posts with visibility 'public', sorted by date descending."
  []
  (filter #(= "public" (:visibility %)) (load-posts)))

(defn posts-by-stage
  "Filter posts by their stage (seedling, growing, evergreen)."
  [stage]
  (filter #(= stage (:stage %)) (load-posts)))

(defn find-post
  "Find a post by slug. Returns post regardless of visibility."
  [slug]
  (get (:by-slug (ensure-cache!)) slug))
