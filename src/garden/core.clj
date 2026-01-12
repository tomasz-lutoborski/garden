(ns garden.core
  "Application entry point."
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [garden.routes :as routes])
  (:gen-class))

(defn- env-truthy?
  [s]
  (when (some? s)
    (not (contains? #{"0" "false" "no" "off"} (.toLowerCase ^String s)))))

(defn- wrap-request-timing
  [handler]
  (fn [request]
    (let [start (System/nanoTime)
          response (handler request)
          elapsed-ms (/ (double (- (System/nanoTime) start)) 1e6)]
      (println (format "%s %s -> %s (%.1fms)"
                       (-> (:request-method request) name .toUpperCase)
                       (:uri request)
                       (:status response)
                       elapsed-ms))
      response)))

(def app
  (cond-> (routes/handler)
    (env-truthy? (System/getenv "GARDEN_TIMING")) wrap-request-timing
    (or (nil? (System/getenv "GARDEN_RELOAD"))
        (env-truthy? (System/getenv "GARDEN_RELOAD"))) wrap-reload))

(defn -main [& _]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Running on http://localhost:" port))
    (jetty/run-jetty app {:port port :join? true})))
