(ns garden.core
  (:require [replicant.dom :as r]
            [garden.ui :as ui]))

;; ------------------------------------------------------------
;; Global state

(defonce store (atom {:message "hello in productivity garden"
                      :todos [{:title "List todos"
                               :note "Render list of all todos"
                               :id (random-uuid)
                               :status :not-done}
                              {:title "Create todo"
                               :note "Be able to create new todo"
                               :id (random-uuid)
                               :status :not-done}]}))

(swap! store assoc :message "heloo")

;; ------------------------------------------------------------
;; Render function

(defn render [state]
  [:main
   [:h1 (:message state)]
   (for [todo (:todos state)]
     (ui/render-todo todo))])

;; ------------------------------------------------------------
;; Main function

(defn ^:export main []
  (let [el (js/document.getElementById "app")]

    #_(r/set-dispatch!
       (fn [event-data handler-data]
         (when (= :replicant.trigger/dom-event (:replicant/trigger event-data))
           (prn :event (:replicant/dom-event event-data)
                :handler handler-data))))

    (r/render el (render @store))

    (add-watch store ::render
               (fn [_ _ _ new-state]
                 (r/render el (render new-state))))))
