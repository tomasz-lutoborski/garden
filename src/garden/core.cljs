(ns garden.core
  (:require [replicant.dom :as r]
            [nexus.registry :as nxr]))

;; --- App state --------------------------------------------------------------

(defonce store (atom {:message "hello in productivity garden"
                      :todos [{:title "List todos"
                               :note "Render list of all todos"
                               :id (random-uuid)
                               :status :not-done}
                              {:title "Create todo"
                               :note "Be able to create new todo"
                               :id (random-uuid)
                               :status :not-done}]}))

;; --- Render function --------------------------------------------------------

(defn render [state]
  [:main
   [:h1.text-xl
    (for [todo (:todos state)]
      [:li (:title todo)])]])

(defn ^:export main []
  (let [store (atom {:message "hello in productivity garden"
                     :todos [{:title "List todos"
                              :note "Render list of all todos"
                              :id (random-uuid)
                              :status :not-done}
                             {:title "Create todo"
                              :note "Be able to create new todo"
                              :id (random-uuid)
                              :status :not-done}]})
        el (js/document.getElementById "app")]

    (add-watch store ::render
               (fn [_ _ _ new-state]
                 (r/render el (render new-state))))))
