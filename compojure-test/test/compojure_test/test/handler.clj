; Run tests with:
;
;   $ lein midje :autotest

(ns compojure-test.test.handler
  (:use clojure.test
        midje.sweet
        ring.mock.request
        compojure-test.handler))

(facts "/main route returns main page"
       (app-routes (request :get "/")) => (contains {:body #"Witaj"}))

(facts "Navigation bar does not return link to the current page."
       (app-routes (request :get "/")) =not=> (contains {:body #"href=\"/\""})
       (app-routes (request :get "/sign-in")) =not=> (contains {:body #"href=\"/sign-in\""}))
