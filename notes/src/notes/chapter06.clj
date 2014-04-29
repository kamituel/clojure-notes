(ns notes.chapter06)

(defprotocol Matrix
	"Protocol for working with 2d datastructures"
	(lookup [matrix i j])
	(update [matrix i j value])
	(rows [matrix])
	(cols [matrix])
	(dims [matrix]))

(extend-protocol Matrix
	clojure.lang.IPersistentVector
	(lookup [vov i j]
		(get-in vov [i j]))
	(update [vov i j value]
		(assoc-in vov [i j] value))
	(rows [vov]
		(seq vov))
	(cols [vov]
		(apply map vector vov))
	(dims [vov]
		[(count vov) (count (first vov))]))

(defn vov
	"Factory function"
	[h w]
	(vec (repeat h (vec (repeat w nil)))))

(extend-protocol Matrix
	nil
	(lookup [vov i j] "lookup over nil")
	(update [vov i j value])
	(rows [vov] [])
	(cols [vov] [])
	(dims [vov] [0 0]))

(defrecord Point [x y])

(defprotocol Measurable
	"A protocol for retrieving the dimensions of widgets."
	(width [measurable] "Returns width in px.")
	(height [measurable] "Returns height in px."))

(defrecord Button [text])

(extend-type Button
	Measurable
	(width [btn]
		(* 8 (-> btn :text count)))
	(height [btn] 8))

(def bordered
	{:width #(* 2 (:border-width %))
	 :height #(* 2 (:border-height %))})

(println (get-in Measurable [:impls Button]))
; {
;	:height #<chapter06$eval206$fn__207 notes.chapter06$eval206$fn__207@46b113c7>, 
;	:width #<chapter06$eval206$fn__209 notes.chapter06$eval206$fn__209@2cd5276a>}

(defn combine
	[op f g]
	(fn [& args]
		(op (apply f args) (apply g args))))

(defrecord BorderedButton [text border-width border-height])

(extend BorderedButton
	Measurable
	(merge-with (partial combine +)
		(get-in Measurable [:impls Button])
		bordered))

(println (merge-with (partial combine +)
		(get-in Measurable [:impls Button])
		bordered))

(println
(let [btn (Button. "Hello World")
	  btn2 (BorderedButton. "Hello World" 6 4)]
	  {:btn [(width btn) (height btn)] :bordered-button [(width btn2) (height btn2)]})
)
; {:btn [88 8], :bordered-button [100 16]}

"Print classes which extend given protocol"
(println (extenders Measurable))
; (notes.chapter06.BorderedButton notes.chapter06.Button)

"Check if any given class extends protocol"
(println (extends? Measurable Button))
; true

"Check if a particular instance satisfies protocol"
(println (satisfies? Measurable (Button. "hello")))
; true

(defn -main
	[]

	"Protocols: defprotocol and extend-protocol"

	(prn (lookup nil 5 5))
	; "Lookup over nil"

	(def vov1 (vov 3 4))
	(prn (lookup (update vov1 1 1 :ok) 1 1))

	"Records: defrecord"

	(prn (Point. 3 4))
	; #notes.chapter06.Point{:x 3, :y 4}

	; Records are associative collections:
	(prn (:x (Point. 3 4)))
	; 3

	(prn (map :x [(Point. 1 2) (Point. 3 4) (Point. 5 6)]))
	; (1 3 5)

	(prn (:z (assoc (Point. 10 20) :z 3)))
	; 3

	; dissoc of auxiliary slots does not degenerate record to map.
	; But dissoc of declared field, does degenerate to map.
	(prn (dissoc (assoc (Point. 1 2) :z 7) :z))
	(prn (dissoc (assoc (Point. 1 2) :z 7) :x))
	; #notes.chapter06.Point{:x 1, :y 2}
	; {:y 2, :z 7}

	"Record constructors"
	; This:
	(Point. 3 4 {:foo :bar} {:z 5})
	; is equivalent to:
	(assoc (with-meta (Point. 3 4) {:foo :bar}) :z 5)

	"Default (implicitly generated) factory function"
	(->Point 3 4)
	(map->Point {:x 3 :y 4 :z 5})

	(map (partial apply ->Point) [[5 6] [7 8] [9 10]])

	"reify - anonymous types"
	(.start (Thread. (reify
		java.lang.Runnable
		(run [this] (
			println "Thread working")))))



	)