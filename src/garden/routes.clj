(ns garden.routes
  "Route definitions."
  (:require [reitit.ring :as ring]
            [garden.views.pages :as pages]))

(def routes
  [["/" {:get #'pages/index-page}]
   ["/post/:slug" {:get #'pages/post-page}]])

(defn router []
  (ring/router routes))

(defn handler []
  (ring/ring-handler (router) #'pages/not-found))
