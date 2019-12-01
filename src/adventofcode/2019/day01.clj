(ns adventofcode.2019.day01
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day01.txt"))

(defn solve-1 []
  (->> (str/split input #"\n")
       (map #(Integer/parseInt %))
       (map (fn [x] (- (int (/ x 3)) 2)))
       (apply +)))

(comment (solve-1))

(defn calculate-module-mass [x]
  (lazy-seq
   (when (> x 0)
     (cons x (calculate-module-mass (- (int (/ x 3)) 2))))))

(defn solve-2 []
  (->> (str/split input #"\n")
       (map #(Integer/parseInt %))
       (mapcat #(->> % calculate-module-mass (drop 1)))
       (apply +)))

(comment (solve-2))
