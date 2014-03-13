(ns euler-totient)

(defn- gcd
  [a b]
  (if (= b 0)
    a
    (gcd b (mod a b))))

(defn- _euler-totient-factors
  [x]
  (reduce
   #(if (= 1 (gcd x %2)) (conj % %2) %)
   []
   (next (range x))))

(defn- _euler-totient
  [x]
  (if (= 1 x) 1 (count (_euler-totient-factors x))))

(defn- single-euler-totient [x]
  "Same as above, but in one function."
  (letfn [(gcd [a b]
               (if (= b 0)
                 a
                 (gcd b (mod a b))))
          (euler-totient-factors [x]
                                 (reduce
                                  #(if (= 1 (gcd x %2)) (conj % %2) %)
                                  []
                                  (next (range x))))]
    (if (= 1 x) 1 (count (euler-totient-factors x)))))

(defn euler-totient
  []

  (prn (= 1 (single-euler-totient 1)))
  (prn (= 16 (single-euler-totient 40)))

  )

