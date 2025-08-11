(ns garden.dev
  (:require [garden.core :as garden]))

(defonce store (atom {:urge-redirects 0
                      :todos [{:title "List todos"
                               :note "Render list of all todos"
                               :id (random-uuid)
                               :status :not-done}
                              {:title "Create todo"
                               :note "Be able to create new todo"
                               :id (random-uuid)
                               :status :not-done}]}))

(defn main []
  (garden/init store)
  (println "Loaded!"))

(defn ^:dev/after-load reload! []
  (garden/init store)
  (println "Reloaded!!"))

(reload!)