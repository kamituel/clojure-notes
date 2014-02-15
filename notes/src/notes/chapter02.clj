(ns notes.chapter02)

(defn -main 
	[]

	"map"
	(= [2 4 6] (map #(* % 2) [1 2 3]))
	(= [10 -200 3000] (map #(* % %2 %3) [1 2 3] [10 100 1000] [1 -1 1]))
	(= ["lorem" "ipsum" "dolor"] (map clojure.string/lower-case ["LoReM" "iPsUm" "dOLoR"]))

	"reduce"
	(= 20 (reduce max [0 20 -4 10]))
	; same as:
	(= 20 (apply max [0 20 -4 10]))
	"reduce with initial value"
	(= 120 (reduce + 100 [10 5 3 2]))
	"reduce - vector of numbers to hash of numbers and it's squares"
	(reduce
		(fn [m item]
			(assoc m item (* item item)))
		{} [1 2 3 4])

	"partials"
	(defn log
		[severity message]
		(println "[" severity "]" message))
	(def debug (partial log "DEBUG"))
	(= (debug "lorem ipsum") (log "DEBUG" "lorem ipsum"))

	"partials using function literals"
	(#(log "DEBUG" %) "lorem ipsum")

	"composition"
	(def miggle-string 
		(comp clojure.string/upper-case 
			clojure.string/trim 
			clojure.string/reverse))
	(= "AZOK" (miggle-string "koza\n"))

	"doseq"
	(doseq [man ["Wilson" "Mark" "Greg"] woman ["Cuddy" "Cameron" "Thirteen"]]
		(println man "loves" woman))

	"composition example: logger"
	(defn print-logger
		[writer]
		#(binding [*out* writer]
			(println %)))

	; Logging to the stdout
	(def *out*-logger (print-logger *out*))
	(*out*-logger "hello")

	; Logging to string buffer
	(def writer (java.io.StringWriter.))
	(def retained-logger (print-logger writer))
	(retained-logger "hello")
	(println (str writer))

	; Logging both to file and stdout
	(defn multi-logger
		[& logger-fns]
		#(doseq [f logger-fns]
			(f %)))
	(def log (multi-logger *out*-logger retained-logger))
	(log "multi hello")
	(println (str writer))

	"memoize (for pure functions)"
	(defn computionally-intensive-function
		[]
		(println "executed"))
	(def memoized (memoize computionally-intensive-function))
	; should print only one
	(memoized)
	(memoized)

	)