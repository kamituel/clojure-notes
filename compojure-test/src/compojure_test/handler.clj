(ns compojure-test.handler
  (:use
   compojure.core
   hiccup.core
   ring.middleware.session
   ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn is-logged-in
  [request]
  (contains? (:session request) :username))

(def navbar-elements
  [{:path "/" :title "Home"}
   {:path "/sign-in" :title "Sign in" :pred (complement is-logged-in)}
   {:path "/sign-out" :title "Sign out" :pred is-logged-in}
   {:path "/about" :title "About"}])

(defn navbar
  "Returns a Hiccup syntax for navigation bar.
  Link for the current site won't be present."
  [elements {uri :uri :as request}]
  (vec (concat [:div] (map
   (fn [{:keys [path title]}]
     [:a {:href path} title])
   (filter #(and
             (not= uri (:path %))
             (cond (:pred %) ((:pred %) request) :else true))
           navbar-elements)))))

(defn layout
  "Page layout"
  [request body]
  (html
   (navbar navbar-elements request)
   body))

(defn main-page [{session :session :as request}]
  (layout request
          [:h1 "Witaj " (:username session)]))

(defn about-page [request]
  (layout request
          [:div "About"]))

(defn sign-in-page [{session :session :as request}]
  (layout request
          (if (:username session)
            [:div "Already logged in"]
            [:form {:action "/sign-in" :method :POST}
             [:button "zaloguj"]])))

(defn sign-in-action [{session :session :as request}]
  (-> (redirect "/")
      (assoc-in [:session :username] "x")))

(defn sign-out-page [{session :session :as request}]
  (-> (redirect "/")
      (assoc :session nil)))

(defroutes app-routes
  (GET "/" [:as request] (main-page request))
  (GET "/sign-in" [:as request] (sign-in-page request))
  (POST "/sign-in" [:as request] (sign-in-action request))
  (GET "/sign-out" [:as request] (sign-out-page request))
  (GET "/about" [:as request] (about-page request))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))


