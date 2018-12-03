(ns adventofcode.2018.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def parsed-file
  (->> (slurp (io/resource "adventofcode/2018/day02.txt"))
       str/split-lines))

(defn some-n [n s]
  (->> (frequencies s)
       (some (fn [[k v]] (if (= n v) {n 1})))))

(defn solve-1 []
  (->> parsed-file
       (map (fn [s] (into (some-n 2 s) (some-n 3 s))))
       (apply merge-with +)
       vals
       (apply *)))

(defn remove-differing-characters [s1 s2]
  (->> (map (fn [c1 c2] (if (= c1 c2) c1 "")) s1 s2)
       str/join))

(defn apply-to-all-pairs [f coll]
  (for [s1 coll s2 coll] (f s1 s2)))

(def id-length (-> parsed-file first count))

(defn solve-2 []
  (->> parsed-file
       (apply-to-all-pairs remove-differing-characters)
       (filter (fn [s] (= (count s) (dec id-length))))
       first))
