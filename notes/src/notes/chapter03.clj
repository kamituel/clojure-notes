(ns notes.chapter03
	(:require clojure.set))

(defn -main
	[]

	"take-nth"
	(= [0 2 4 6] (take-nth 2 (apply vector (range 8))))
	(= [0 3 6] (take-nth 3 (apply vector (range 8))))

	"drop"
	(= '(2 3) (drop 1 '(1 2 3)))

	"interleave"
	(interleave [1 2 3] ["a" "b" "c"])
	; (1 "a" 2 "b" 3 "c")

	"empty - returns empty collection of the same type as a collection given as argument."
	(= [] (empty [1 2 3]))
	(= {} (empty {:a 1 :b 2}))
	(= '() (empty '(1 2 3)))
	(= #{} (empty #{1 2 3}))

	"empty - returns collection of the type it was given as parameter, thanks to 'empty'"
	(defn swap-pairs
		[sequential]
		(into (empty sequential)
			(interleave 
				(take-nth 2 (drop 1 sequential))
				(take-nth 2 sequential))))
	(swap-pairs [1 2 3 4 5 6])
	; [2 1 4 3 6 5]
	(swap-pairs '(1 2 3 4 5 6))
	; (5 6 3 4 1 2)

	"count"
	(count [1 1 1])
	; 3
	(count {:a 1 :b 2 :c 3})
	; 3

	"Sequences: seq, first, rest, next"
	(first "Clojure")
	; \C
	(rest "Clojure")
	; (\l \o \j \u \r\ e)
	(next "C")
	; nil
	(rest "C")
	; ()

	"cons vs conj: cons returns new sequence with item prepender. conj returns new colleciton with item added."
	(cons 10 [1 2 3])
	; (10 1 2 3)	it's a seq
	(conj [1 2 3] 10)
	; [1 2 3 10]	it's a vector

	"list* - produce seq with any number of head values (instead of one as in cons)"
	(= 
		(list* 1 2 3 ["a" "b" "c"])
		; (1 2 3 "a" "b" "c") 
		(cons 1 (cons 2 (cons 3 ["a" "b" "c"]))))

	"lazy sequences"
	(defn lazy-random
		[limit]
		(lazy-seq 
			(println "Generating random")
			(cons
				(rand-int limit) 
				(lazy-random limit))))
	(take 3 (lazy-random 10))			; generates three random numbers
	(take 10 (lazy-random 10))			; generates ten random numbers

	"forcing realization of sequence"
	(dorun (take 5 (lazy-random 100)))
	; "generating random" printed 5 times. nil returned

	(doall (take 5 (lazy-random 100)))
	; "generating random" printed 5 times. seq returned (with five random numbers)

	"maps - assoc/dissoc/get"
	(def m {:a 1 :b 2 :c 3})
	(get m :d "default value")
	; "default value"
	(let [m (assoc m :d 4)]
		(get m :d "default-value"))
		; 4
	(dissoc m :b)
	; {:a 1, :c 3}

	; assigning/designing multiple entries at once
	(dissoc m :a :b)
	; {:c 3}

	(assoc m
		:g 1
		:h 2)
	; {:h 2, :g 1, :a 1, :c 3, :b 2}

	"assoc/get can be used with vectors as well"
	(def v [1 2 3])
	(assoc v 0 "a")
	; ["a" 2 3]
	(assoc v 3 "a")
	; [1 2 3 "a"]
	(get v 1)
	; 2

	"contains? - check for the presence of the given KEY (not value) in a collection"
	(contains? [1 2 3] 0)
	; true					because vector contains 0th element (not the value 0)
	(contains? [5 5 5] 5)
	; false					because vector contains only three elements (so keys 0,1,2)
	(contains? {:a 1 :b 2} :a)
	; true
	(contains? {:a 1 :b 2} 2)
	; false					because "2" is the value for :b, but not the key

	"get returns nil when key is not present, but it can return nil when it's a value as well"
	(get {:a nil} :a) 
	; nil
	"how to thell whether there is a key with 'nil' value or there is no such key? Use find."
	(find {:a nil} :a)
	; [:a nil]
	(find {:a nil} :b)
	; nil

	"nth - similar to get, but will throw IndexOutOfBoundsException instead of returning nil. And works only with numerical indexes."
	; (nth [1 1 1] 3)
	; throws 

	"stacks - lists and vectors + conj/pop/peek"
	(conj '(1 2 3) "a")
	; ("a" 1 2 3)
	(pop '(1 2 3))
	; (2 3)
	(peek '(1 2 3))
	; 1

	"disj - remove value from the set"
	(disj #{1 2 3 4} 1 3)
	; #{2 4}

	"clojure.set namespace for useful operations on sets"
	(clojure.set/union #{1 2 3} #{2 3 4})
	; #{1 2 3 4}
	(clojure.set/subset? #{1 2 3} #{2 3})
	; false
	(clojure.set/superset? #{1 2 3} #{2 3})
	; true
	(clojure.set/subset? #{2 3} #{1 2 3})
	; true
	(clojure.set/superset? #{2 3} #{1 2 3})
	; false
	(clojure.set/intersection #{1 2 3} #{2 3 4 5})
	; #{2 3}
	(clojure.set/project #{ {:name "Jorges" :age 27} {:name "Luis" :age 72} } [:name])
	; #{{:name "Jorges"} {:name "Luis"}}




	)