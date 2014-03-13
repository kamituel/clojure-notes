(ns primes
  (:use [clojure.set]))

(defn- sieve
  [n & other]

  (let [upper-bound 1000]
    (letfn [(sieve-round
             [primes candidates]
             (if (empty? candidates)
               [primes, candidates]
               (let [prime (first candidates)
                     multiples (range prime upper-bound prime)]
                 [(conj primes prime) (clojure.set/difference candidates multiples)])))]
      (let [prime-numbers (or (first other) [])
            candidates (or (second other) (apply sorted-set (range 2 upper-bound)))]
        (cond
         (= n (count prime-numbers)) (prn prime-numbers)
         (= 0 (count candidates)) (prn "Could not generate " (str n) " primes")
         :else (do
            (apply sieve n (sieve-round prime-numbers candidates)))))))

  )

(defn- primes-bertrand
  [n]
  (if (= 1 n)
    [2]
    (let [pp (primes-bertrand (dec n))]
      (conj pp
            (first (filter
                    (fn [i] (every? #(< 0 (mod i %)) pp))
                    (range (last pp) (* 2 (last pp)))))))))

(defn primes
  [n]
  ;(sieve (read-string n))
  (prn (primes-bertrand (read-string n)))
  )
