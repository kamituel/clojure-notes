(ns palindromic-numbers)

(defn- _palindromic_number
  [x]
  (let [number-to-seq (fn [x] (map read-string (-> x str (clojure.string/split #"") next)))
        seq-to-number (fn [xs] (-> "" (clojure.string/join xs) read-string))
        make-palindrom (fn [len half] (concat half (if (even? len) (reverse half) (next (reverse half)))))
        xs (number-to-seq x)
        len (count xs)
        xs-half (take (/ len 2) xs)
        xs-next-half (number-to-seq (inc (seq-to-number xs-half)))
        xs-palindrom (seq-to-number (make-palindrom len xs-half))
        xs-next-palindrom (seq-to-number (make-palindrom len xs-next-half))
        result (if (>= xs-palindrom x) xs-palindrom xs-next-palindrom)]
  (cons result (lazy-seq (_palindromic_number (inc result))))))

(defn palindromic-numbers
  []
  (prn (take 5 (_palindromic_number 161)))
  (prn (take 5 (_palindromic_number 162)))
  (prn (take 5 (_palindromic_number 1660)))
  (prn (take 5 (_palindromic_number 1672))))
