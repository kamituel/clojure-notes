(ns fourclojure)

(defn- manual
  [excercises]
  (do
    (println "Available excercises:")
    (doseq [ex excercises]
      (println " " ex))))

(defn -main
  [id & args]

  (def cls-dir (clojure.java.io/file "src/"))
  (def excercises (map #(->
                         (.getName %)
                         ;(clojure.string/replace "_" "")
                         (clojure.string/replace ".clj" "")
                         (clojure.string/replace "_" "-"))
                       (filter #(not (.startsWith (.getName %) "fourclojure"))
                          (file-seq cls-dir))))


  (println "For:" id)
  (doseq [excercise (filter #(<= 0 (.indexOf % id)) excercises)]
    (println "  Executing: " excercise)

    (require (symbol excercise))
    (apply (resolve (symbol (str excercise "/" excercise))) args))
  )
