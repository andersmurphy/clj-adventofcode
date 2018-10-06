(ns adventofcode.2017.day05
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn maze-from-string [input-string]
  (mapv read-string (string/split input-string #"\n")))

(defn next-step [current-position maze steps]
  (let [current-position-value (get maze current-position)]
    (if-not current-position-value
      steps
      (recur
       (+ current-position current-position-value)
       (assoc maze current-position (inc current-position-value))
       (inc steps)))))

(defn solve-maze [input-string]
  (next-step 0 (maze-from-string input-string) 0))

(def input-string
  (string/trim-newline
   (slurp (io/resource "adventofcode/2017/day05.txt"))))

;; (solve-maze input-string)
