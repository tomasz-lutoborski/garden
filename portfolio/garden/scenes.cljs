(ns garden.scenes
  (:require [portfolio.ui :as portfolio]
            [portfolio.replicant :refer-macros [defscene]]
            [garden.ui.todo :as ui]))

(defscene done-todo
  (ui/render-ui {:title "some todo" :status :done}))

(defscene not-done-todo
  (ui/render-ui {:title "some todo" :status :not-done}))

(defn main []
  (portfolio/start!
   {:config
    {:css-paths ["/styles.css"]
     :viewport/defaults
     {:background/background-color "#fdeddd"}}}))
