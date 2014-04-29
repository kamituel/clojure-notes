(ns notes.chapter07type)

(defmulti run
  "Executes the computation."
  type)

(defmethod run Runnable
  [x]
  (println "Running as Runnable")
  (.run x))

(defmethod run java.util.concurrent.Callable
  [x]
  (println "Running as Callable")
  (.call x))

(prefer-method run java.util.concurrent.Callable Runnable)

(defmethod run :runnable-map
  [m]
  (println "Running from map")
  (run (:run m)))

(defn -main
  []

  (run #(println "hello!"))
  ; Running as Callable
  ; hello!

  (run (reify Runnable
         (run [this] (println "hello!"))))
  ; Running as Runnable
  ; hello!

  (run ^{:type :runnable-map}
       {:run #(println "hello!") :other :data})
  ; Running from map
  ; Running as Callable
  ; hello!


  )