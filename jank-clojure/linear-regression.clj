(defrecord LinearRegression [lr n-iters weights bias])

(defn dot-product [a b]
  (reduce + (map * a b)))

(defn mat-vec-dot [matrix vec]
  (map #(dot-product % vec) matrix))

(defn transpose [matrix]
  (apply mapv vector matrix))

(defn scalar-mul [scalar vec]
  (mapv #(* scalar %) vec))

(defn vec-add [a b]
  (mapv + a b))

(defn vec-sub [a b]
  (mapv - a b))

(defn sum [v]
  (reduce + v))

(defn fit [model X y]
  (let [n-samples (count X)
        n-features (count (first X))
        weights (vec (repeat n-features 0.0))
        bias 0.0
        lr (:lr model)
        n-iters (:n-iters model)]
    (loop [i 0 w weights b bias]
      (if (>= i n-iters)
        (assoc model :weights w :bias b)
        (let [y-pred (vec-add (mat-vec-dot X w) (repeat n-samples b))
              error (vec-sub y-pred y)
              dw (scalar-mul (/ 1.0 n-samples)
                             (map #(dot-product % error) (transpose X)))
              db (/ (sum error) n-samples)
              new-w (vec-sub w (scalar-mul lr dw))
              new-b (- b (* lr db))]
          (recur (inc i) new-w new-b))))))

(defn predict [model X]
  (vec-add (mat-vec-dot X (:weights model))
           (repeat (count X) (:bias model))))

;; Example usage
(def X [[1.0] [2.0] [3.0] [4.0]])
(def y [2.0 4.0 6.0 8.0])

(def model (->LinearRegression 0.01 1000 nil nil))
(def trained (fit model X y))

(println "Weights:" (:weights trained))
(println "Bias:" (:bias trained))
(println "Predictions:" (predict trained X))
