(ns garden.dev
  (:require [garden.core :as garden]
            [nexus.action-log :as action-log]
            [dataspex.core :as dataspex]))

(defonce store (atom {}))
(def el (js/document.getElementById "app"))
(dataspex/inspect "App store" store)
(action-log/inspect)

(defn main ^:dev/after-load []
  (garden/main store el)
  (println "Loaded!"))
