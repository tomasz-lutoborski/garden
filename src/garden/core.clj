(ns garden.core
  "Application entry point."
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [garden.routes :as routes])
  (:gen-class))

(def app
  (-> (routes/handler)
      (wrap-reload)))

(defn -main [& _]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Running on http://localhost:" port))
    (jetty/run-jetty app {:port port :join? true})))
