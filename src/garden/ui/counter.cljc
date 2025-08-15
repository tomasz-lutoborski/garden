(ns garden.ui.counter)

(defn render-ui [state]
  (let [clicks (get-in state [:counter :clicks])]
    [:div.todo-card
     [:h2.text-xl (str "Number of urges redirected: " clicks)]
     [:button.btn
      {:on
       {:click [[::inc [:clicks]]]}}
      "Urge redirected!"]]))