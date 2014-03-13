(defproject compojure-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [lein-light-nrepl "0.0.16"]]
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler compojure-test.handler/app}
  :profiles {
             :dev {
                   :dependencies [
                                  [javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]
                                  [hiccup "1.0.5"]
                                  [midje "1.6.3"]]
                   :plugins [
                             [lein-midje "3.1.1"]]}})
