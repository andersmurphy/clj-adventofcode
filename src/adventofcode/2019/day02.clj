(ns adventofcode.2019.day02
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day02.txt"))

(def clean-input
  (->> (str/split (str/trim input) #",")
       (map #(Integer/parseInt %))))

(defn compute [nums]
  (->> (partition-all 4 nums)
       vec
       (reduce (fn [nums [op a b output]]
                 (if-not (= op 99)
                   (assoc nums output (({1 +, 2 *} op) (nums a) (nums b)))
                   (reduced nums)))
               nums)))

(defn set-initial [a b nums]
  (-> (assoc (vec nums) 1 a)
      (assoc 2 b)))

(defn solve-1 [noun verb]
  (->> clean-input
       (set-initial noun verb)
       compute
       first))

(comment (solve-1 12 2))

(defn checksum [[_ noun verb]]
  (+ (* 100 noun) verb))

(defn solve-2 []
  (->> (for [noun (range 100)
             verb (range 100)]
         [(solve-1 noun verb) noun verb])
       (filter (fn [[result]] (= result 19690720)))
       first
       checksum))

(comment (solve-2))
