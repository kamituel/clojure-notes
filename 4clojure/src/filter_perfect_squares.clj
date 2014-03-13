(ns filter-perfect-squares)

(defn- _filter-perfect-squares
  "Valid for not so large numbers only."
  [s]
  (->>
   #","
   (clojure.string/split s)
   (map read-string)
   (filter (fn [n] (some #(and (= (* % %) n) (< % n)) (->> (range) (drop 1) (take n)))))
   (clojure.string/join ",")))

(defn filter-perfect-squares
  []

  (prn (_filter-perfect-squares "4,5,6,7,8,9"))

  )
