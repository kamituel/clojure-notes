(ns iterate1)

(defn _iterate
  [f x]
  (reductions
     (fn [a _] (f a))
     x
     (lazy-seq (_iterate f (f x)))))

(defn iterate1
  []

  (println (= (take 5 (_iterate #(* 2 %) 1)) [1 2 4 8 16]))

  )
