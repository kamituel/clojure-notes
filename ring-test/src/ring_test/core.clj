(ns ring-test.core
  (:use ring.adapter.jetty
        ring.util.response
        ring.middleware.file
        ring.middleware.content-type
        ring.middleware.params
        ring.middleware.session))

;; --- Simple handler.

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (:remote-addr request)})

(defn add-custom-header [handler header-name header-value]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers header-name] header-value))))

(def app
  (-> handler
      (add-custom-header "Kamil" "Leszczuk")
      (add-custom-header "Ble" "Elb")))

;; --- Creating response using functions, not maps.

(defn handler2 [request]
  (-> (response "<h1>Hello World from a fn!</h1>")
      (content-type "text/html")))

(defn handler3 [request]
  (-> (response "Some content")))

;; --- Serve static content.

(def app3
  (-> handler3
      (wrap-file "resources/")))

;; --- Content types based on extension in the URI.

(def app4
  (-> app3
      (wrap-content-type {:mime-types {"foo" "text/x-foo"}})))

;; --- Query parameters.

(defn handler5 [request]
  (-> (response
       (str
        "You supplied following query string parameters:<br />"
        (clojure.string/join
         "<br />"
         (->> (:query-params request) (map #(str (first %) "=" (second %)))))))
      (content-type "text/html")))

(def app5
  (-> handler5
      wrap-params))

;; --- Sessions.

(defn session-handler [{session :session}]
  (let [count (:count session 0)
        session (assoc session :count (inc count))]
    (-> (response (str "You accessed this page " count " times."))
        (assoc :session session))))

(def app6
  (wrap-session session-handler))

;; --- Main

(defn -main
  []
  (println "Hello, World!")
  (run-jetty app6 {:port 3000}))


