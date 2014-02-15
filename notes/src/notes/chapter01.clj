(ns notes.chapter01)

(defn -main
  "I don't do a whole lot."
  [x & rest]
  (println x "Hello, World!")
  (println rest)

  "Operators are functions. Those two are equivalent:"
  (= (+ 1 2) (apply + [1 2]))

  "You can represent numbers in various bases using '2r' prefix, i.e.:"
  (= 2r110 6)

  "Vectors are functions of their indices"
  (def v [1 2 3])
  (= 3 (v 2))

  "Sequential destructuring"
  (def vv [1 "foo" 3 ["a", "b"] 5 6])
  (let [[el1 _ el2 [letter1 letter2] & rest :as original] vv]
  	(println (format "%d" (+ el1 el2)))
  	(println letter1)
  	(println rest)
  	(println original))

  "Map destructuring"
  (def m {:a 1 :b 2 
  	:c {:a 1 :b 2}
  	:d ["a" "b" "c"]})
  (let [{first :a second :b [_ b _] :d} m]
  	(println b))

  "Map destructuring - avoiding repetition"
  (def m1 {:name "Kamil" :sex "Male" :age 27})
  (def m2 {"name" "Kamil" "sex" "Male" "age" 27})
  (def m3 {'name "Kamil" 'sex "Male" 'age 27})
  (let [{:keys [name sex age]} m1]
  	(println (format "He's name is %s, he's %d years old %s" name age sex)))
  (let [{:strs [name sex age]} m2]
  	(println (format "He's name is %s, he's %d years old %s" name age sex)))
  (let [{:syms [name sex age]} m3]
  	(println (format "He's name is %s, he's %d years old %s" name age sex)))


  "Destructuring more complex data: vector with hash inside. Commented out line is correct, but more verbose."
  (def account ["kamituel" 1986 :name "Kamil" :location "Poland"])
  ;(let [[username birth_year & rest] account {:keys [name location]} rest]
  (let [[username birth_year & {:keys [name location]}] account]
  	(println (format "Username: %s birth: %d name: %s" username birth_year name)))

  "Functions are values and are returned by 'fn' macro. def + fn = defn"
  (def add10 
  	(fn add10 
  		[x] 
  		(+ 10 x)))
  (defn add10 
  	[x]
  	(+ 10 x))
  (= 15 (add10 5))

  "Functions (fn and defn) use destructuring."
  (defn sum-all-but-first
  	[first & rest]
  	(apply + rest))
  (= 10 (sum-all-but-first 10 2 4 4))

  "Example: search or create"
  (defn get-car
  	[& [car-id]]
  	{:car-id (or car-id (str (java.util.UUID/randomUUID)))})
  (println (get-car))
  (println (get-car "porsche"))

  "Keyword arguments (initialization object)"
  (defn make-car
  	[brand & {:keys [color abs seats] :or {color "black" abs false seats 5}}]
  	{:brand brand
  	 :color color
  	 :abs abs
  	 :seats seats})
  (make-car "Ferrari")
  ; Any order of arguments (besides first). 
  (make-car "Ferrari" :seats 2 :abs true)

  "Function literals"
  (= 25 (#(* % %) 5))

  "'%' equals to '%1'"
  (= 25 (#(* %1 %1) 5))

  "'%&' for variable arguments (variadic) function"
  (= 81 (#(apply * %&) 3 3 3 3))

  "Conditionals: if"
  (= "is true" (if true "is true" "is false"))
  
  "Conditionals: when"
  (when true (println "a") (println "b") (println "c"))

  "Conditionals: cond"
  (defn sigma
  	[x]
  	(cond
  		(< x 0) -1
  		(> x 0) 1
  		:else 0))
  (= 1 (sigma 6))

  "Looping: 'recur'. If possible, avoid it and use more high-level funcitons like doseq, dotimes and map/reduce/for/... for collecitons."
  "'recur' DOES NOT consume stack space."
  (loop [x 5]
  	(if (pos? x)
  		(do
  			(println (str "x is " x))
  			x
  			(recur (dec x))
  			)))

  " 'var'"
  (def some-var 5)
  some-var
  "This: "
  (var some-var)
  "Is same as this:"
  #'some-var

  "Java interoperability"
  "Object instantiation:"
  (java.util.ArrayList. 100)		; new java.util.ArrayList(100)
  (Math/pow 2 10)					; Math.pow(2, 10)
  (.substring "hello" 1 3)			; "hello".substring(1,3)
  (Integer/MAX_VALUE)				; Integer.MAX_VALUE


)
