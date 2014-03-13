(ns juxtaposition)

(defn- _juxt [& fns]
  (fn [& args]
    (map #(apply % args) fns)))

(defn juxtaposition []

  (println (= [21 6 1] ((_juxt + max min) 2 3 5 1 6 4)))

  )
