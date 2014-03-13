(ns reductions1)

(defn- _reductions
  ([f [x & xs]] (_reductions f x xs))
  ([f val [x & xs]] (cons
                   val
                   (if x
                      (lazy-seq (_reductions f (f val x) xs))))))

(defn reductions1 []
  (prn (= [0 1 3 6 10] (take 5 (_reductions + (range)))))
  (prn (= [[1] [1 2] [1 2 3] [1 2 3 4]] (_reductions conj [1] [2 3 4])))
  )
