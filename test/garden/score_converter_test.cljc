(ns garden.score-converter-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [garden.scoring :as scoring]))

(deftest task-time->score
  (testing "Calculates score of given activity performed for given time"
    (is (= 100 (scoring/score-converter 10)))))
