(ns notes.chapter07c)

(def fill-hierarchy (-> (make-hierarchy)
                        (derive :input.radio ::checkable)
                        (derive :input.checkbox ::checkable)
                        (derive ::checkable :input)
                        (derive :input.text :input)
                        (derive :input.hidden :input)))

(defn- fill-dispatch [node value]
  (if-let [type (and (= :input (:tag node))
                     (-> node :attrs :type))]
    (keyword (str "input." type))
    (:tag node)))

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
                      derive (fill-dispatch node value) :input)
      (fill node value))
    (assoc node :content [(str value)])))

(defmethod fill :input
  [node value]
  (assoc-in node [:attrs :value] (str value)))

(defmethod fill ::checkable
  [node value]
  (if (= value (-> node :attrs :value))
    (assoc-in node [:attrs :checked] "checked")
    (update-in node [:attrs] dissoc :checked)))

(defn -main
  []

  (def chbox1 (fill {:tag :input
         :attrs {:value "first choice"
                 :type "checkbox"}}
        "first choice"))
  ; {:attrs {:checked "checked", :type "checkbox", :value "first choice"}, :tag :input}

  (fill chbox1 "off")
  ; {:attrs {:type "checkbox", :value "first choice"}, :tag :input}

  (fill {:tag :input
         :attrs {:type "date"}}
        "some date input")
  ; {:attrs {:value "some date input", :type "date"}, :tag :input}

  )