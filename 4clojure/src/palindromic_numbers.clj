(ns palindromic-numbers)

(defn- _palindromic-numbers
  [x])

(defn palindromic-numbers
  []

  (prn (take 16 (_palindromic-numbers 162))
   [171 181 191 202
    212 222 232 242
    252 262 272 282
    292 303 313 323])

  )

(let [x 162
      n (count (str x))
      xs (map read-string (-> x str (clojure.string/split #"") next))
      first-half (take (/ n 2) xs)
      palindromic (concat first-half (reverse first-half))]
  palindromic)

(let [x 123]
  (drop 1 (clojure.string/split (str x) #"")))
