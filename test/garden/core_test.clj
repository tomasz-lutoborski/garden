(ns garden.core-test
  (:require [clojure.test :refer [deftest is testing]]
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
      (is (= "Just body content" (:body result))))))

(deftest index-page-test
  (testing "returns 200 status"
    (let [response (pages/index-page {})]
      (is (= 200 (:status response)))
      (is (= "text/html" (get-in response [:headers "Content-Type"]))))))

(deftest post-page-test
  (testing "returns 404 for non-existent post"
    (let [response (pages/post-page {:path-params {:slug "non-existent-post-xyz"}})]
      (is (= 404 (:status response))))))
