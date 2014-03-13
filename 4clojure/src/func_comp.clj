(ns func-comp)

(defn- fcomp
  [& fns]
  (fn [& args]
    (reduce
       (fn [result f2] (f2 result))
       (-> fns last (apply args))
       (-> fns reverse rest)))
  )

(defn func-comp
  []

  (prn ((fcomp rest reverse) [1 2 3 4]))
  ; (3 2 1)

  )
