(defproject web-scraper "0.1.0-SNAPSHOT"
  :description "Simple concurrent web scraper"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/core.async "1.6.681"]
                 [clj-http "3.12.3"]]
  :main ^:skip-aot web-scraper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})