(ns adventofcode.2017.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn convert-string->array-of-arrays-of-integers [input-string]
  (map #(->> (string/split % #"\t")
             (map read-string))
       (string/split input-string #"\n")))

(defn solve-checksum [input-string]
  (apply + (map
            (comp #(- (last %) (first %)) sort)
            (convert-string->array-of-arrays-of-integers input-string))))

(def input-string
  (string/trim-newline
   (slurp (io/resource "adventofcode/2017/day02.txt"))))

;; (solve-checksum input-string)
