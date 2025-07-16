(def xv 5)

;; FUNCTION EXAMPLE
(defn square [x]
  (println "Squaring" x)
  (* x x))

;; Usage:
(let [x (do (println "Calculating x") xv)]
  (square x))

;; Output:
;; Calculating x
;; Squaring 5
;; => 25

;; MACRO EXAMPLE
(defmacro square-macro [x]
  (println "Macro expanding at compile time!")
  `(let [x# ~x]
     (println "Squaring" x#)
     (* x# x#)))

;; Usage:
(let [x (do (println "Calculating x") xv)]
  (square-macro x))

;; Output:
;; Macro expanding at compile time!   (printed during compilation)
;; Calculating x
;; Squaring 5
;; => 25