(ns notes.chapter04
	(:use clojure.pprint)
	(:use clojure.repl))

(defn -main
	[]

	"delay/deref"
	(def d (delay (println "Running...")
		:done!))

	(realized? d)
	; false

	(println @d)
	(realized? d)
	; true

	; Second dereference will not print "Running..." - d's value has 
	; been cached.
	(println @d)
	(println (deref d))

	"futures"
	(def long-calculation (future (apply + (range 1e4))))
	;@long-calculation

	"timeout and default timeout value"
	(def very-long-calculation (future (apply + (range 1e8))))
	;(deref very-long-calculation 50 :too-long-bro!)
	; :too-long-bro!

	"pmap"
	(defn phone-numbers
		[string]
		(re-seq #"(\d{3})[\.-]?(\d{3})[\.-]?(\d{4})" string))
	(def files (repeat 100 (apply str
		(concat (repeat 1000000 \space)
			"Sunil: 617.555.2937, Betty: 508.555.2218"))))
	;(time (dorun (map phone-numbers files)))

	"watches"
	(defn echo-watch
		[key identity old new]
		(println key old "=>" new))

	(def sarah (atom {:name "Sarah" :age 25}))
	(add-watch sarah :echo echo-watch)
	(swap! sarah update-in [:age] inc)

	"watches - change history"
	(def history (atom ()))
	(defn log->list
		[dest-atom key source old new]
		(when (not= old new)
			(swap! dest-atom conj new)))

	(add-watch sarah :record (partial log->list history))
	(swap! sarah assoc-in [:age] 35)
	(swap! sarah assoc-in [:wears-glasses?] true)
	(swap! sarah update-in [:age] dec)
	(pprint @history)

	"validators"
	(def age (atom 18 :validator pos?))
	(swap! age + 500)
	;(reset! age 0)				; throws java.lang.IllegalStateException: Invalid reference state


	"refs"

	(defn character
		[name & {:as opts}]
		(ref (merge {:name name :items #{} :health 500} opts)))

	(def smaug (character "Smaug" :strength 400 :items (set (range 50))))
	(def bilbo (character "Bilbo" :health 100 :strength 100))
	(def gandalf (character "Gandalf" :health 75 :mana 750))

	(defn loot
		[from to]
		(dosync
			(when-let [item (first (:items @from))]
				(alter to update-in [:items] conj item)
				(alter from update-in [:items] disj item))))

	(while (loot smaug bilbo))

	(println (map (comp count :items deref) [bilbo gandalf]))
	(println (filter (:items @bilbo) (:items @gandalf)))

	"io! - to make sure no unsafe operations are performed in a transaction"
	;(dosync
	;	(io!
	;		(println "not allowed, it's inside of a transaction.")
	;		))

	"ref history"
	(comment
		(def a (ref 0))
		(future (dotimes [_ 500] (dosync (Thread/sleep 20) (alter a inc))))
		(println "'a' = " @(future (dosync (Thread/sleep 1000) @a)))
		(println (str (ref-history-count a)))

		(def b (ref 0 :min-history 50 :max-history 100))
		(future (dotimes [_ 500] (dosync (Thread/sleep 20) (alter b inc))))
		(println "'b' = " @(future (dosync (Thread/sleep 1000) @b)))
		(println (str (ref-history-count b))))

	"vars"
	; private var
	(def ^:private a1 0)
	; Same as:
	;(def ^{:private true} a1 0)

	(deref (var a1)) ; same as @#'a1

	"docstrings"
	(def some-variable "Doc for some-variable" 1)
	(clojure.repl/doc some-variable)
	(def ^{:doc "Doc for some-variable2"} some-variable2  2)
	(clojure.repl/doc some-variable2)

	"constants"
	; TODO - it's not working
	(def ^:const max-value 255)
	(defn valid-value?
		[x]
		(<= x max-value))
	(println (valid-value? 290))
	(def max-value 500)
	(println (valid-value? 290))

	"dynamic bindings"
	(def ^:dynamic *response-code* nil)
	(defn http-get
		[url-string]
		(when (thread-bound? #'*response-code*)
			(set! *response-code* 404)))
	
	; Prints "nil"
	(http-get "fake")
	(println *response-code*)

	; Prints "404"
	(binding [*response-code* nil]
		(http-get "fake")
		(println *response-code*))

	


	)