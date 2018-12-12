(ns adventofcode.2018.day05
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def parsed-data
  (-> (slurp (io/resource "adventofcode/2018/day05.txt"))
      str/trim))

(def regex-for-polarity
  "Creates regex of the form aA|Aa|bB|Bb..."
  (->> (mapcat
        (fn [s] [(str s (str/upper-case s)) (str (str/upper-case s) s)])
        "abcdefghijklmnopqrstuvwxyz")
       (interpose "|")
       (apply str)
       re-pattern))

(defn react
  ([current] (react "" current))
  ([prev current]
   (if (= prev current)
     (count current)
     (recur current (str/replace current regex-for-polarity "")))))

(defn solve-1 []
  (-> parsed-data react))

(defn solve-2 [] nil)
