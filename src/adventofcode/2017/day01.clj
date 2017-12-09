(ns adventofcode.2017.day01
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-string (string/trim-newline (slurp (io/resource "adventofcode/2017/day01.txt"))))

(defn convert-string-to-array-of-numbers
  [put-string]
  (map (comp read-string str) (seq input-string)))

(defn count-neighbouring-numbers
  [[head & tail] previous result]
  (if-not head
    result
    (recur tail head (if (= head previous)
                       (+ head result)
                       result))))

(defn solve-captcha
  [input-string]
  (let [array-of-numbers (convert-string-to-array-of-numbers input-string)]
    (count-neighbouring-numbers array-of-numbers (last array-of-numbers) 0)))
