; This example build upon 07c. Page 312.

(ns notes.chapter07d)

(def fill-hierarchy (-> (make-hierarchy)
                        (derive :input.radio ::checkable)
                        (derive :input.checkbox ::checkable)
                        (derive ::checkable :input)
                        (derive :input.text :input)
                        (derive :input.hidden :input)))

(defn- fill-dispatch [node value]
  (if-let [type (and (= :input (:tag node))
                     (-> node :attrs :type))]
    [(keyword (str "input." type)) (class value)]
    [(:tag node) (class value)]))

(defmulti fill
  "Fill a xml/html node (as per clojure.xml)
  with the provided value."
  #'fill-dispatch
  :default nil
  :hierarchy #'fill-hierarchy)

; Dynamically adjusts hierarchy to cover all input.* types.
(defmethod fill nil
  [node value]
  (if (= :input (:tag node))
    (do
      (alter-var-root #'fill-hierarchy
                      derive (first (fill-dispatch node value)) :input)
      (fill node value))
    (assoc node :content [(str value)])))

(defmethod fill [:input Object]
  [node value]
  (assoc-in node [:attrs :value] (str value)))

(defmethod fill [::checkable clojure.lang.IPersistentSet]
  [node value]
  (if (contains? value (-> node :attrs :value))
    (assoc-in node [:attrs :checked] "checked")
    (update-in node [:attrs] dissoc :checked)))

(defn -main
  []

  (fill {:tag :input
         :attrs {:value "yes"
                 :type "checkbox"}}
        "yes")
  ; nothing happend - still not checked.
  ; {:attrs {:type "checkbox", :value "yes"}, :tag :input}

  (fill {:tag :input
         :attrs {:value "yes"
                 :type "checkbox"}}
        #{"yes" "y"})
  ; {:attrs {:checked "checked", :type "checkbox", :value "yes"}, :tag :input}


  )