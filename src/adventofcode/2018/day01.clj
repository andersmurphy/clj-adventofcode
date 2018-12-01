(ns adventofcode.2018.day01
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn split-on-new-line [s]
  (str/split s #"\n"))

(->> (slurp (io/resource "adventofcode/2018/day01.txt"))
     split-on-new-line
     (map #(Integer/parseInt %))
     (apply +))
