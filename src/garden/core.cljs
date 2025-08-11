(ns garden.core
  (:require [replicant.dom :as r]
            [garden.ui.todo :as todo]
            [garden.ui.counter :as counter]
            [garden.ui.layout :as layout]))

(def views
  [{:id :todos
    :text "TODOs"}
   {:id :counters
    :text "Achievement counters"}])

(defn get-current-view [state]
  (:current-view state))

;; ------------------------------------------------------------
;; Render function

(defn render [state]
  (let [current-view (get-current-view state)]
    [:main.flex.gap-4.flex-col.p-12
     (layout/tab-bar current-view views)
     (case current-view
       ;;  :todos
       ;;  (for [todo (:todos state)]
       ;;    (todo/render-ui todo))

       :counters
       (counter/render-ui state)

       [:h1.text-lg "Choose your activity"])]))


(defn perform-actions [state event-data]
  (mapcat
   (fn [action]
     (prn (first action) (rest action))
     (or (counter/perform-action state action)
         (case (first action)
           :action/assoc-in
           [(into [:effect/assoc-in] (rest action))]

           "unknown action")))
   event-data))

(defn process-effects [store [effect & args]]
  (case effect
    :effect/assoc-in
    (apply swap! store assoc-in args)))

;; ------------------------------------------------------------
;; Main function

(defn init [store]
  (let [el (js/document.getElementById "app")]

    (r/set-dispatch!
     (fn [_ event-data]
       (->> (perform-actions @store event-data)
            (run! #(process-effects store %)))))


    (add-watch store ::render
               (fn [_ _ _ new-state]
                 (r/render el (render new-state))))

    (swap! store assoc :loaded-at (.getTime (js/Date.)))))


(comment
  (layout/tab-bar :todos views))