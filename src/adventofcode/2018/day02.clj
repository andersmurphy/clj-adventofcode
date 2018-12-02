(ns adventofcode.2018.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-file []
  (->> (slurp (io/resource "adventofcode/2018/day02.txt"))
       str/split-lines))

(defn count-n [n s]
  (->> (frequencies s)
       (some (fn [[k v]]
               (if (= n v)
                 {(keyword (str n "s")) 1})))))

(defn solve-1 []
  (->> (parse-file)
       (map #(into (count-n 2 %) (count-n 3 %)))
       (apply merge-with +)
       vals
       (apply *)))

(defn solve-2 []
  nil)
