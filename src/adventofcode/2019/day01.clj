(ns adventofcode.2019.day01
  (:require [clojure.string :as str]))

(def input (->> (str/split (slurp "resources/adventofcode/2019/day01.txt") #"\n")
                (map #(Integer/parseInt %))))

(defn calculate-fuel-mass [x]
  (- (quot x 3) 2))

(defn solve-1 []
  (->> (map calculate-fuel-mass input)
       (apply +)))

(comment (solve-1))

(defn solve-2 []
  (->> (mapcat (fn [x] (->> (iterate calculate-fuel-mass x)
                            (take-while #(> % 0))
                            (drop 1)))
               input)
       (apply +)))

(comment (solve-2))
