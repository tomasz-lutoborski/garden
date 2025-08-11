(ns garden.ui.counter)

(defn perform-action [state [action]]
  (when (= ::urge-redirected action)
    [[:effect/assoc-in [:urge-redirects] (inc (:urge-redirects state))]]))

(defn render-ui [{:keys [urge-redirects]}]
  [:div.todo-card
   [:h2.text-xl (str "Number of urges redirected: " urge-redirects)]
   [:button.btn
    {:on
     {:click [[::urge-redirected]]}}
    "Urge redirected!"]])