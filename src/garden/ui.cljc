(ns garden.ui)

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

(defn render-todo [data]
  [:div.todo-card
   [:h2.text-xl.text-indigo-950 (:title data)]
   [:input.checkbox.checkbox-lg
    {:type "checkbox"
     :checked (= (:status data) :done)}]])