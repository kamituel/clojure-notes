(ns pascal-triangle)

(defn- _pascal-triangle
  [xs]
  (let [xs2 (vec (concat [0] xs [0]))
        xs-next (for [i (range (dec (count xs2)))
                      :let [a (get xs2 i)
                            b (get xs2 (inc i))]]
                  (+' a b))]
    (cons xs (lazy-seq (_pascal-triangle xs-next)))))

;; And better solution ;)
(defn- _pt
  [xs]
  (lazy-seq
   (let [xs-next (map +' (cons 0 xs) (concat xs [0]))]
     (cons xs (_pt xs-next)))))

(defn- _nthrow
  [x]
  (let [factorial #(reduce * 1 (range (inc %)))
        binomial #(/ (factorial %1) (* (factorial %2) (factorial (- %1 %2))))])
  (map #(binomial % x) (range 0 x)))

(defn pascal-triangle
  []

  (prn (take 100 (_pascal-triangle [2 4 2])))
  (prn (take 100 (_pt [2 4 2])))
  (prn (_nthrow 5))

  )
