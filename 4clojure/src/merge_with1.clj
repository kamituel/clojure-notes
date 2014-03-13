(ns merge-with1)

(defn- _merge-with
  [f & maps]
  (let [ks (set (mapcat keys maps))]
    (reduce (fn [res k]
              (let [vals (filter (comp not nil?) (map #(% k) maps))]
                (assoc res k (if (= 1 (count vals)) (first vals) (apply f vals)))))
            {}
            ks)))

(defn merge-with1
  []

  (prn (= (_merge-with * {:a 2, :b 3, :c 4} {:a 2} {:b 2} {:c 5})
   {:a 4, :b 6, :c 20}))

  (prn (= (_merge-with - {1 10, 2 20} {1 3, 2 10, 3 15})
   {1 7, 2 10, 3 15}))

  )
