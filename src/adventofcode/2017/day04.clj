(ns adventofcode.2017.day04
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-string (string/trim-newline (slurp (io/resource "adventofcode/2017/day04.txt"))))

(defn split-into-lines
  [text]
  (string/split text #"\n"))

(defn split-into-words
  [line]
  (string/split line #"\s"))

(defn count-occurance-of-word-in-line
  [search-word, line]
  (reduce
   (fn [acc, word]
     (if (= word search-word)
       (inc acc)
       acc))
   0
   (split-into-words line)))

(defn line-contains-duplicate-words
  [line]
  (some
   (fn [count] (> count 1))
   (map
    (fn [word] (count-occurance-of-word-in-line word line))
    (split-into-words line))))

(defn count-truthy
  [coll]
  (count (filter identity coll)))

(defn solve-valid-passphrases
  [input-string]
  (count-truthy
   (map
    (comp not line-contains-duplicate-words)
    (split-into-lines input-string))))
