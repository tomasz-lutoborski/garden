(ns garden.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [garden.core :as core]))

(deftest parse-frontmatter-test
  (testing "parses frontmatter with title and date"
    (let [content "---\ntitle: Hello World\ndate: 2025-01-01\n---\nBody content"
          result (core/parse-frontmatter content)]
      (is (= "Hello World" (get-in result [:meta "title"])))
      (is (= "2025-01-01" (get-in result [:meta "date"])))
      (is (= "Body content" (:body result)))))

  (testing "returns empty meta when no frontmatter"
    (let [result (core/parse-frontmatter "Just body content")]
      (is (= {} (:meta result)))
      (is (= "Just body content" (:body result))))))

(deftest index-page-test
  (testing "returns 200 status"
    (let [response (core/index-page {})]
      (is (= 200 (:status response)))
      (is (= "text/html" (get-in response [:headers "Content-Type"]))))))

(deftest post-page-test
  (testing "returns 404 for non-existent post"
    (let [response (core/post-page {:path-params {:slug "non-existent-post-xyz"}})]
      (is (= 404 (:status response))))))
