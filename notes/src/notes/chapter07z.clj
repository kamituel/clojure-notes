; Note that having mutlimethod whose behaviour is not strictly
; dependent upon the values provided as arguments is, by definition,
; not idempotent. It'll be more difficult to understand, test and compose.

(ns notes.chapter07z)

(def priorities (atom {:911-call :high
                       :evaucation :high
                       :pothole-report :low
                       :tree-down :low}))

(defmulti route-message
  (fn [message] (@priorities (:type message))))

(defmethod route-message :low
  [{:keys [type]}]
  (println (format "Oh, there's another %s. Put it in the log." (name type))))

(defmethod route-message :high
  [{:keys [type]}]
  (println (format "Alert the authorities, there's a %s!" (name type))))

(defn -main
  []

  (route-message {:type :911-call})
  ; Alert the authorities, there's a 911-call!

  (route-message {:type :tree-down})
  ; Oh, there's another tree-down. Put it in the log.

  (swap! priorities assoc :tree-down :high)

  (route-message {:type :tree-down})
  ; Alert the authorities, there's a tree-down!

  )
