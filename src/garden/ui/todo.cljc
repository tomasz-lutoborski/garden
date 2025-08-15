(ns garden.ui.todo)

(defn change-todo-status [todos id]
  (mapv
   (fn [m] (if (= (:id m) id)
             (assoc m :status (case (:status m)
                                :done :not-done
                                :not-done :done))
             m))
   todos))

(defn delete-todo [todos id]
  (filter
   (fn [m] (not= (:id m) id))
   todos))

(defn perform-action [state [action & args]]
  (let [[id] args
        todos (:todos state)]
    (case action
      ::task-status-change
      [[:effect/assoc-in [:todos] (change-todo-status todos id)]]

      ::task-delete
      [[:effect/assoc-in [:todos] (delete-todo todos id)]]

      nil)))


(def check-icon
  [:svg
   {:width "24px",
    :height "24px",
    :stroke-width "1.5",
    :viewBox "0 0 24 24",
    :fill "none",
    :xmlns "http://www.w3.org/2000/svg",
    :color "#000000"}
   [:path
    {:d "M5 13L9 17L19 7",
     :stroke "#000000",
     :stroke-width "5",
     :stroke-linecap "round",
     :stroke-linejoin "round"}]])

(def render-add-todo
  [:form.todo-card
   [:input.input.input-lg.w-128
    {:type "text"
     :placeholder "What do you plan to do?"}]
   [:input.btn
    {:type "submit"
     :on {:click [[::task-create "hello"]
                  [:actions/prevent-default]]}}]])

(defn render-ui [todos]
  [:div.flex.gap-4.flex-col
   (for [todo todos]
     (let [{:keys [title status id]} todo]
       [:div.todo-card
        [:h2.text-xl.text-indigo-950 title]
        [:div.flex.gap-4.justify-center.items-center
         [:input.checkbox.checkbox-lg
          {:type "checkbox"
           :checked (= status :done)
           :on {:click [[::task-status-change id]]}}]
         [:button.btn
          {:on {:click [[::task-delete id]]}}
          "🗑️"]]]))
   render-add-todo])

(comment
  (def sample-todos [{:title "List todos"
                      :note "Render list of all todos"
                      :id 1
                      :status :not-done}
                     {:title "Create todo"
                      :note "Be able to create new todo"
                      :id 2
                      :status :not-done}])

  (filter
   (fn [m] (not= (:id m) 1))
   sample-todos)

  (def sample-store {:urge-redirects 0
                     :todos [{:title "List todos"
                              :note "Render list of all todos"
                              :id 1
                              :status :not-done}
                             {:title "Create todo"
                              :note "Be able to create new todo"
                              :id 2
                              :status :not-done}]})
  (perform-action sample-store [::task-status-change 1]))
              