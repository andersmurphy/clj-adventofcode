(ns adventofcode.2019.day04
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day04.txt"))

(defn format-input [input]
  (->> (str/split (str/trim input) #"-")
       (map #(Integer/parseInt %))))

(defn int->seq [x]
  (seq (str x)))

(defn find-valid-codes [preds [start end]]
  (->> (range start (inc end))
       (map int->seq)
       (filter
        (apply every-pred preds))))

(defn count-passwords [preds]
  (->> input
       format-input
       (find-valid-codes preds)
       count))

(defn ascending? [xs]
  (= xs (sort xs)))

(defn contains-double? [xs]
  (not= xs (dedupe xs)))

(defn solve-1 []
  (count-passwords [ascending? double?]))

(defn contains-double-not-part-of-a-larger-group? [xs]
  (->> (partition-by identity xs)
       (some #(= (count %) 2))))

(defn solve-2 []
  (count-passwords [ascending? contains-double-not-part-of-a-larger-group?]))
