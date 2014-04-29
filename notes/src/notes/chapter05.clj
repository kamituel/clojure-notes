(ns notes.chapter05
	(:require 
		[clojure.string :as str]
		[clojure.walk :as walk]))

(defmacro reverse-it
	[form]
	(walk/postwalk #(if (symbol? %) 
		(symbol (str/reverse (name %)))
		%)
	form))

(defn -main
	[]

	(reverse-it
		(qesod [gra (egnar 5)]
			(nltnirp (cni gra))))

	(macroexpand-1 `(reverse-it
		(qesod [gra (egnar 5)]
			(nltnirp (cni gra)))))

	"syntax-quoting vs quoting"
	; syntax quoting uses fully qualified names (i.e. with namespaces)
	(def foo 123)
	[foo (quote foo) 'foo `foo]
	; [123 foo foo notes.chapter05/foo]

	"unquoting"
	; All those three are equivalent:
	(list `map `println [foo])
	`(map println [~foo])
	`(map println ~[foo])

	; Unquoting a list or vector unquotes the enitre form.
	`(apply + ~(range (rand-int 10)))
	; (clojure.core/apply clojure.core/+ (0 1 2 3 4 5 6))

	"unquote-splicing"
	; This:
	(let [defs '((def x 123)
				(def y 321))]
	(concat (list 'do) defs))
	; (do (def x 123) (def y 321))

	; Can be written as:
	(let [defs '((def x 123)
				(def y 321))]
	`(do ~@defs))

	"gensyms"
	"WARN: TODO: for some reason, those do not work properly in lein. Use REPL instead."
	(defmacro unhygenic
		[& body]
		`(let [~'x :oops]
			~@body))

	; hygenic and hygenic-short are equivalent.
	(comment
	(defmacro hygenic
		[& body]
		(let [sym (gensym)]
			`(let [~sym :macro-value]
				~@body)))

	(defmacro hygenic-short
		[& body]
		`(let [x# :macro-value]
			~@body))

	(let [x :important-value]
		(unhygenic (println "x:" x)))

	(let [x :important-value]
		(hygenic (println "x:" x)))

	(let [x :important-value]
		(hygenic-short (println "x:" x)))
	)

	"Double evaluation"
	(comment
	(defmacro spy [x]
		`(do 
			(println "Spied" '~x ~x)
			~x))

	(spy 2)
	; Spied 2 2
	; 2
		
	(spy (rand-int 10))
	; Spied (rand-int 10) 1
	; 2

	(macroexpand-1 '(spy (rand-int 10)))
	; (do 
	;	(clojure.core/println "Spied" (quote (rand-int 10)) (rand-int 10)) 
	;	(rand-int 10))

	; Solution - use gensyms
	(defmacro spy2 [x]
		`(let [x# ~x]
			(println "Spied" '~x x#)
			x#))

	(spy2 (rand-int 10))
	; Spied (rand-int 10) 3
	; 3

	; We could also avoid using gensyms
	(defn spy-helper [expr value]
		(println expr value)
		value)

	(defmacro spy3 [x]
		`(spy-helper '~x ~x))

	(spy3 (rand-int 10))
	; (rand-int 10) 2
	; 2
	)

	"&env"
	(defmacro simplify 
		[expr]
		(let [locals (set (keys &env))]
			(if (some locals (set (flatten expr)))
				expr
				(do
					(println "simplyfing")
					;(list `quote (eval expr))
					(list `quote (eval expr))
					))))

	(defn f
		[a b c]
		(+ a b c (simplify (apply + (range 5e7)))))

	(f 1 2 3)

	"Testing macros - use macroexpand-1, but if macro is using &env or &form you can't because "
	"macroexpand-1 won't allow mocking &form/&env values. But, internally, macros are currently implemented as functions:"
	; First argument is &form, second is &env, rest of arguments are macro arguments.
	(@#'simplify nil {} '(inc 1))
	; simplyfing
	; (quote 2)

	(@#'simplify nil {'x 40} '(inc x))
	; (inc x)


	"Threading macros: -> and ->>"
	(defn ensure-seq [x]
		(if (seq? x)
			x
			(list x)))

	(defn insert-second [x ys]
		(let [ys (ensure-seq ys)]
			(concat (list (first ys) x) (rest ys))))

	; or, shorter:
	(defn insert-second [x ys]
		(let [ys (ensure-seq ys)]
			`(~(first ys) ~x ~@(rest ys))))

	; or even shorter ;)
	(defn insert-second [x ys]
		(let [ys (ensure-seq ys)]
			(list* (first ys) x (rest ys))))

	(insert-second 5 '(1 2 3 4))
	; (1 5 2 3 4)

	; Now, "thread" macro (->)
	(defmacro thread
		([x] x)
		([x form] (insert-second x form))
		([x form & more] `(thread (thread ~x ~form) ~@more)))

	(thread [1 2 3] (conj 4) reverse println)

	; Usage
	(-> [7 0 4] (conj 2) (conj 11) (conj 0) sort reverse)
	; (11 7 4 2 0 0)

	(->> [7 0 4] (map inc) (reduce +))
	; 14

	(->> [7 0 4] (map #(str "I'm " % " years old")))
	; ("I'm 7 years old" "I'm 0 years old" "I'm 4 years old")

	(let [files (vec (range 10))]
		(vec
			(flatten
				(reverse
					(partition-all 3 files)))))

	(let [files (vec (range 10))]
		(->> files (partition-all 3) reverse flatten vec))
	; [9 6 7 8 3 4 5 0 1 2]


	)