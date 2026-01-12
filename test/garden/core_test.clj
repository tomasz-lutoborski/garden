(ns garden.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [garden.posts :as posts]
            [garden.views.pages :as pages]))

(deftest parse-frontmatter-test
  (testing "parses frontmatter with title and date"
    (let [content "---\ntitle: Hello World\ndate: 2025-01-01\n---\nBody content"
          result (posts/parse-frontmatter content)]
      (is (= "Hello World" (get-in result [:meta "title"])))
      (is (= "2025-01-01" (get-in result [:meta "date"])))
      (is (= "Body content" (:body result)))))

  (testing "returns empty meta when no frontmatter"
    (let [result (posts/parse-frontmatter "Just body content")]
      (is (= {} (:meta result)))
      (is (= "Just body content" (:body result)))))

  (testing "parses stage and visibility fields"
    (let [content "---\ntitle: Test\nstage: growing\nvisibility: unlisted\n---\nBody"
          result (posts/parse-frontmatter content)]
      (is (= "growing" (get-in result [:meta "stage"])))
      (is (= "unlisted" (get-in result [:meta "visibility"]))))))

(deftest index-page-test
  (testing "returns 200 status"
    (let [response (pages/index-page {})]
      (is (= 200 (:status response)))
      (is (= "text/html" (get-in response [:headers "Content-Type"]))))))

(deftest post-page-test
  (testing "returns 404 for non-existent post"
    (let [response (pages/post-page {:path-params {:slug "non-existent-post-xyz"}})]
      (is (= 404 (:status response))))))

(deftest posts-cache-test
  (testing "does not re-parse posts when unchanged"
    (posts/clear-cache!)
    (let [md-count (->> (file-seq (io/file posts/posts-dir))
                        (filter #(.isFile ^java.io.File %))
                        (filter #(str/ends-with? (.getName ^java.io.File %) ".md"))
                        count)
          parse-count (atom 0)
          orig-load-post posts/load-post]
      (with-redefs [posts/load-post (fn [file]
                                      (swap! parse-count inc)
                                      (orig-load-post file))]
        (doall (posts/public-posts))
        (is (= md-count @parse-count))
        (doall (posts/public-posts))
        (is (= md-count @parse-count))))))

(deftest stage-badge-test
  (testing "returns correct emoji for each stage"
    (is (= "🌱" (pages/stage-badge "seedling")))
    (is (= "🌿" (pages/stage-badge "growing")))
    (is (= "🌳" (pages/stage-badge "evergreen")))
    (is (= "🌱" (pages/stage-badge "unknown")))))
