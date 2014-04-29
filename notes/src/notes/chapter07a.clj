(ns notes.chapter07a)

(defmulti fill1
	"Fill a xml/html node (as per clojure.xml)
	with the provided value."
  (fn [node value] (:tag node))
  :default :not-specified            ; :default is ":default" by default. Can be overriden here.
  )

(defmethod fill1 :div
  [node value]
  (assoc node :content [(str value)]))

(defmethod fill1 :input
  [node value]
  (assoc-in node [:attrs :value] (str value)))

"Default fn in case :default wasn't set."
(defmethod fill1 :default
  [node value]
  (assoc node :content [(str value)]))

"Default fn in case :default was set."
(defmethod fill1 :not-specified
  [node value]
  (assoc node :content "Not specified"))

(defn -main
	[]

  (fill1 {:tag :div} "hello")
  ; {:content ["hello"], :tag :div}

  (fill1 {:tag :input} "hello")
  ; {:attrs {:value "hello"}, :tag :input}

  (fill1 {:span :input} "hello")
  ; {:content ["hello"], :span :input}

  "In case :default was set."
  (fill1 {:tag :not-specified} "hello")
  ; {:content "Not specified", :tag :not-specified}



	)
