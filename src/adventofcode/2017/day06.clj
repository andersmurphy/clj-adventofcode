(ns adventofcode.2017.day06
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn split-into-banks [input-string]
  (mapv read-string (string/split input-string #"\t")))

(defn has-cycle-happened-before? [current-cycle previous-cycles]
  (some #(= current-cycle %) previous-cycles))

(defn find-index-of-largest-bank [banks]
  (.indexOf banks (apply max banks)))

(defn sprinkle-blocks [previous-position blocks banks]
  (if (zero? blocks)
    banks
    (let [current-position
          (if (get banks (inc previous-position))
            (inc previous-position)
            0)]
      (recur
       current-position
       (dec blocks)
       (update banks current-position inc)))))

(defn redistribute-cycle [banks, previous-cycles]
  (if (has-cycle-happened-before? banks previous-cycles)
    (count previous-cycles)
    (let [largest-bank-index (find-index-of-largest-bank banks)]
      (recur
       (sprinkle-blocks
        largest-bank-index
        (get banks largest-bank-index)
        (assoc banks largest-bank-index 0))
       (conj previous-cycles banks)))))

(defn solve-redistribution-cycles [input-string]
  (redistribute-cycle
   (split-into-banks input-string)
   []))

(def input-string
  (string/trim-newline
   (slurp (io/resource "adventofcode/2017/day06.txt"))))

;; (solve-redistribution-cycles input-string)
