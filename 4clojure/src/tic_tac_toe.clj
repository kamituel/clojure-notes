(ns tic-tac-toe)

(defn _tic-tac-toe
  [[& rows]]

  (let [dimension (count rows)
        take-diagonal (fn [index-fn] (reduce (fn [d n] (conj d (nth (nth rows n) (index-fn n)))) [] (take dimension (range))))
        diagonals [(take-diagonal identity) (take-diagonal (partial - dimension 1))]
        columns (apply mapv vector rows)
        result (ffirst (filter #(apply = %) (concat rows columns diagonals)))]
    (if (= :e result) nil result)))

(defn tic-tac-toe
  []

  (prn (= :x (_tic-tac-toe [[:x :o :x]
                            [:e :x :o]
                            [:x :e :x]])))

  (prn (= :o (_tic-tac-toe [[:x :o :o]
                            [:e :x :o]
                            [:x :e :o]])))

  (prn (= nil (_tic-tac-toe [[:x :o :x]
                             [:e :o :o]
                             [:x :e :x]])))

  (prn (= :o (_tic-tac-toe [[:x :o :x]
                            [:o :o :o]
                            [:x :e :x]])))

  (prn (= :o (_tic-tac-toe [[:x :o :x :e :e]
                            [:o :o :o :o :o]
                            [:x :e :x :o :e]
                            [:e :e :e :x :o]
                            [:x :e :o :e :e]])))

  (prn (= :x (_tic-tac-toe [[:x :o :x :e :x]
                            [:o :o :o :x :o]
                            [:x :e :x :o :e]
                            [:e :x :e :x :o]
                            [:x :e :o :e :e]])))

  )
