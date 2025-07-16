(ns web-scraper.core
  (:require [clojure.core.async :as async :refer [go chan <! >! timeout go-loop]]
            [clj-http.client :as http])
  (:gen-class))

;; 1. Atom for thread-safe result aggregation
(def results (atom []))

;; 2. Channel-based worker function
(defn fetch-url [url c]
  (go
    (try
      (println "Fetching:" url)
      (let [response (http/get url {:throw-exceptions false})]
        (println "Completed:" url "Status:" (:status response))
        (>! c {:url url :status (:status response) :size (count (:body response))}))
      (catch Exception e
        (println "Error fetching:" url "Error:" (.getMessage e))
        (>! c {:url url :error (.getMessage e)})))))

;; 3. Main controller function
(defn scrape-urls [urls]
  (let [c (chan (count urls))]  ; Result channel
    
    ;; Launch parallel fetch jobs
    (doseq [url urls]
      (fetch-url url c))
    
    ;; Aggregate results
    (go-loop [completed 0]
      (when-let [result (<! c)]
        (swap! results conj result)  ; Atomically update results
        (let [new-completed (inc completed)]
          (if (< new-completed (count urls))
            (recur new-completed)
            (do
              (println "\nScrape completed!")
              (async/close! c))))))
    
    ;; Return results atom immediately
    results))

;; 4. Execute the scraper
(def urls-to-scrape
  ["https://clojure.org" 
   "https://openjdk.org" 
   "https://python.org"
   "https://nodejs.org"])

(defn -main [& args]
  (let [urls (if (seq args) args urls-to-scrape)]
    (println "Starting scraper with URLs:" urls)
    (reset! results [])  ; Clear previous results
    
    (let [scraped-data (scrape-urls urls)]
      
      ;; 5. Monitor progress (in another thread)
      (future 
        (while (< (count @scraped-data) (count urls))
          (println "Progress:" (count @scraped-data) "/" (count urls))
          (Thread/sleep 500))
        (println "Final results:")
        (doseq [res @scraped-data]
          (println res))))))

;; For REPL usage
(defn demo []
  (reset! results [])
  (let [scraped-data (scrape-urls urls-to-scrape)]
    (future 
      (while (< (count @scraped-data) (count urls-to-scrape))
        (println "Progress:" (count @scraped-data) "/" (count urls-to-scrape))
        (Thread/sleep 500))
      (println "Final results:")
      (doseq [res @scraped-data]
        (println res)))
    scraped-data))