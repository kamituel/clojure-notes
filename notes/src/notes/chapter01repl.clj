(ns notes.chapter01repl)

(defn -main
	[]
	(print (str (ns-name *ns*) ">>> "))
	(flush)
	(let [expr (read)
		  value (eval expr)]
		(when (not= :quit value)
			(println value)
			(recur))))