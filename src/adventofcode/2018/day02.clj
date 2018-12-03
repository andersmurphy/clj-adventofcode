(ns adventofcode.2018.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-file []
  (->> (slurp (io/resource "adventofcode/2018/day02.txt"))
       str/split-lines))

(defn some-n [n s]
  (->> (frequencies s)
       (some (fn [[k v]] (if (= n v) {n 1})))))

(defn solve-1 []
  (->> (parse-file)
       (map (fn [s] (into (some-n 2 s) (some-n 3 s))))
       (apply merge-with +)
       vals
       (apply *)))

(defn solve-2 []
  nil)
