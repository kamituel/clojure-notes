(ns notes.chapter03)

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

	


	)