(ns garden.dev
  (:require [garden.core :as garden]
            [dataspex.core :as dataspex]))

(defonce store (atom {}))

(dataspex/inspect "App store" store)

(defn main []
  (garden/init store)
  (println "Loaded!"))

(defn ^:dev/after-load reload! []
  (garden/init store)
  (println "Reloaded!!"))

(reload!)