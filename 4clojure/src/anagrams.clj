(ns anagrams)

(defn- _anagrams
  [words]
  (->>
   words
   (group-by #(apply str (sort %)))
   vals
   (filter (fn [ws] (< 1 (count ws))))
   (map set)
   set
   ))

(defn anagrams
  []

  (prn (= #{#{"meat" "team" "mate"}}
          (_anagrams ["meat" "mat" "team" "mate" "eat"])))
  (prn (= #{#{"veer" "ever"} #{"lake" "kale"} #{"mite" "item"}}
          (_anagrams ["veer" "lake" "item" "kale" "mite" "ever"])))

  )
