(ns garden.core
  (:require [replicant.dom :as r]
            [garden.ui.todo :as todo]
            [nexus.registry :as nxr]
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
       :todos
       (todo/render-ui (:todos state))

       :counters
       (counter/render-ui state)

       [:h1.text-lg "Choose your activity"])]))

(nxr/register-action!
 ::counter/inc
 (fn [state path]
   [[:store/assoc-in path (inc (get-in state path))]]))

(nxr/register-effect!
 :store/assoc-in
 ^:nexus/batch
 (fn [_ store path-values]
   (swap! store
          (fn [state]
            (reduce (fn [s [path value]]
                      (assoc-in s path value)) state path-values)))))

(nxr/register-effect!
 :actions/prevent-default
 (fn [{:keys [dispatch-data]}]
   (some-> dispatch-data :replicant/dom-event .preventDefault)))

(nxr/register-action!
 ::todo/task-create
 (fn [arg1 arg2 arg3]
   (prn arg1 arg2 arg3)))

(nxr/register-system->state! deref)
;; ------------------------------------------------------------
;; Main function

(defn main [store el]
  (r/set-dispatch!
   (fn [dispatch-data actions]
     (nxr/dispatch store dispatch-data actions)))


  (add-watch store ::render
             (fn [_ _ _ new-state]
               (r/render el (render new-state))))

  (swap! store assoc :loaded-at (.getTime (js/Date.))))


(comment
  (layout/tab-bar :todos views)
  (defonce store (atom {:urge-redirects 0
                        :todos [{:title "List todos"
                                 :note "Render list of all todos"
                                 :id 1
                                 :status :not-done}
                                {:title "Create todo"
                                 :note "Be able to create new todo"
                                 :id 2
                                 :status :not-done}]})))