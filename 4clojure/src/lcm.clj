(ns lcm)

(defn _lcm [& xs]
   ((fn _self [xs original]
      (if (apply = xs)
        (first xs)
        (let [[min-index min-value] (->> xs
                              (map-indexed vector)
                              (apply min-key second))]
          (_self (concat
                  (take min-index xs)
                  (list (+ min-value (nth original min-index)))
                  (take-last (- (count xs) (inc min-index)) xs)) original)
          ))) xs xs))

(defn lcm
  []

  (prn (_lcm 5 3 7))

  )
