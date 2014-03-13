; Examples of use for some of clojure.core library functions.

(ns corelib)

(defn _aget
  []
  ; TODO:
  "TODO"
  )

(defn _fnil
  []
  (let [plusnil (fnil + 100)]
    (=
     (plusnil 100 3)
     (plusnil nil 3)
     (+ 100 3))))

(defn _fnext
  []
  (=
   2
   (fnext [1 2 3 4 5])))

(defn _ffirst
  []
  (let [xs [["a" "b" "c"] [1 2 3] [:x :y :z]]]
    (=
     (first (first xs))
     (ffirst xs)
     "a"
     )))

(defn _filterv
  "filter which returns a vector, no matter what type col was given as input."
  []
  (=
    [1 5 2]
    (filterv pos? '(-1 0 1 -5 5 2))))

(defn _frequencies
 []
 (=
  {1 3, 2 2, 3 4}
  (frequencies [1 1 1 2 2 3 3 3 3])))

(defn _group-by
  []
  (=
   {1 ["a" "b"]
    2 ["ab" "re"]
    8 ["lollipop"]}
   (group-by count ["a" "ab" "b" "lollipop" "re"])))

(defn _hash
  []
  (=
   (hash [1 2 3])
   30817))

(defn _into
  []
  (=
   [1 2 3 4 5]
   (into [1 2 3] [4 5])))

(defn _if-not
  []
  (if-not nil true false))

(defn _iterate
  []
  (=
   [1 2 4 8 16 32 64 128]
   (take 8 (iterate #(* 2 %) 1))))

(defn _interleave
  []
  (=
   [1 "a" :x 2 "b" :y]
   (interleave [1 2 3] ["a" "b" "c" "d" "e"] [:x :y])))

(defn _interpose
  []
  (=
   [1 "a" 2 "a" 3]
   (interpose "a" [1 2 3])))

(defn _juxt
  "((juxt a b c) x) => [(a x) (b x) (c x)]"
  []
  (=
   [11 9 20 30]
   ((juxt inc dec (partial * 2) (partial * 3)) 10)))

(defn _keep
  []
  (=
   [1 3]
   (keep :a [{:a 1} {:b 2} {:a 3 :b 4}])))

(defn _key
  []
  (=
   :b
   (key (second {:a 1 :b 2}))))

(defn _keys
  []
  (=
   ["b" :a :c]
   (keys {:a 1 "b" 2 :c 3})))

(defn _merge-with
  []
  (=
   {:a 1 :b 12 :c 103}
   (merge-with + {:a 1 :b 2 :c 3} {:b 10} {:c 100})))

(defn _nthrest
  []
  (=
   [4 5 6]
   (nthrest [1 2 3 4 5 6] 3)))

(defn _reductions
  []
  (= [[] [0] [0 1] [0 1 2] [0 1 2 3]]
     (take 5 (reductions conj [] (range)))))

(defn _second
  []
  (= 2
     (second [1 2 3 4 5])))

(defn _trampoline
  []
  "TODO")

(defn _zipmap
  []
  (= {:a 1 :b 2 :c 3}
     (zipmap [:a :b :c] [1 2 3])))

(defn corelib
  []

  (doseq [f [_aget
             _fnil
             _fnext
             _ffirst
             _filterv
             _frequencies
             _group-by
             _hash
             _into
             _if-not
             _iterate
             _interleave
             _interpose
             _juxt
             _keep
             _key
             _keys
             _merge-with
             _nthrest
             _reductions
             _second
             _trampoline
             _zipmap]] (println f (f)))

  )
