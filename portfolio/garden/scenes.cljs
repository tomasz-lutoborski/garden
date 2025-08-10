(ns garden.scenes
  (:require [portfolio.ui :as portfolio]
            [portfolio.replicant :refer-macros [defscene]]
            [garden.ui :as ui]))

(defscene done-todo
  (ui/render-todo {:title "some todo" :status :done}))

(defscene not-done-todo
  (ui/render-todo {:title "some todo" :status :not-done}))

(defn main []
  (portfolio/start!
   {:config
    {:css-paths ["/styles.css"]
     :viewport/defaults
     {:background/background-color "#fdeddd"}}}))
