(ns word-sorting
  (:require [clojure.string :as str]))

(defn- _word-sorting
  [s]
  ;; Could be shorter when used with sort-by.
  (sort
   #(compare (str/upper-case %1) (str/upper-case %2))
   (-> s (str/replace #"[^\w\s]" "") (str/split #"\s+"))))

(defn word-sorting
  []

  (prn (_word-sorting  "Clojure is a fun language!")
   ["a" "Clojure" "fun" "is" "language"])

  )
