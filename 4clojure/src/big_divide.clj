(ns big-divide)

(defn- _big-divide
  [n a b]
  (let [qa (quot (-' n 1) a)
        qb (quot (-' n 1) b)
        ab (* a b)
        qab (quot (-' n 1) ab)]
    (println "n" n "a" a "b" b "qa" qa "qb" qb "ab" ab "qab" qab)
    (+'
     (*' (/ a 2) (+' 1 qa) qa)
     (*' (/ b 2) (+' 1 qb) qb)
     (- (*' (/ ab 2) (+' 1 qab) qab)))))

(defn big-divide
  []

  (prn (_big-divide 3 17 11))
  (prn (_big-divide 10 3 5))
  (prn (_big-divide 1000 3 5))
  (prn (_big-divide (* 10000 10000 10000) 757 809))

  )
