(ns garden.score-converter-test
  ;; In CLJS, bring in test macros
  #?(:cljs (:require-macros [cljs.test :refer [deftest is testing]]))
  (:require
   ;; In CLJ use clojure.test, in CLJS use cljs.test (for fns)
   #?(:clj  [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test    :refer [deftest is testing]])
   [garden.scoring :as scoring]))

(deftest task-time->score
  (testing "Calculates score of given activity performed for given time"
    (is (= 100 (scoring/score-converter 10)))))
