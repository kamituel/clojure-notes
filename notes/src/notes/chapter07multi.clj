; In case when multimethod is called with value which is implementing
; two interfaces which are specified in two multimethods' methods.

(ns notes.chapter07multi)

(defmulti run
  "Executes the computation."
  class)

(defmethod run Runnable
  [x]
  (println "Running as Runnable")
  (.run x))

(defmethod run java.util.concurrent.Callable
  [x]
  (println "Running as Callable")
  (.call x))

(defn -main
  []

  ; (run #(println "hello!"))
  ;   Exception in thread "main" java.lang.IllegalArgumentException:
  ;   Multiple methods in multimethod 'run' match dispatch value:
  ;   class notes.chapter07multi$_main$fn__27 -> interface java.util.concurrent.Callable
  ;   and interface java.lang.Runnable, and neither is preferred

  ; Now, multimethod "run" will prefer "Callable" over "Runnable" interface.
  (prefer-method run java.util.concurrent.Callable Runnable)
  (run #(println "hello!"))
  ; "hello!"

  )