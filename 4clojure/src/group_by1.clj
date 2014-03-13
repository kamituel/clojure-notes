(ns group-by1)

(defn- _group-by1
  [f xs]
  (reduce
   (fn [res [val el]]
     (assoc res val (conj (or (res val) []) el)))
   {}
   (map (fn [x] [(f x) x]) xs)))

(defn- _group-by1-merge-with
  [f xs]
  (apply merge-with concat (map (fn [x] (hash-map (f x) [x])) xs)))

(defn group-by1
  []

  (prn (= (_group-by1 #(> % 5) [1 3 6 8]) {false [1 3], true [6 8]}))

  (prn (= (_group-by1 #(apply / %) [[1 2] [2 4] [4 6] [3 6]])
   {1/2 [[1 2] [2 4] [3 6]], 2/3 [[4 6]]}))

  (prn (_group-by1-merge-with #(apply / %) [[1 2] [2 4] [4 6] [3 6]]))

  )
