(ns adventofcode.2017.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-string (string/trim-newline (slurp (io/resource "adventofcode/2017/day02.txt"))))

(defn convert-string-to-array-of-arrays-of-integers
  [input-string]
  (map
   (comp
    (fn [row] (map read-string row))
    (fn [row] (string/split row #"\t")))
   (string/split input-string #"\n")))

(defn solve-checksum
  [input-string]
  (apply + (map
            (comp
             (fn [row] (- (last row) (first row)))
             sort)
            (convert-string-to-array-of-arrays-of-integers input-string))))
