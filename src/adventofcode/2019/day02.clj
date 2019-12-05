(ns adventofcode.2019.day02
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day02.txt"))

(defn format-input [input]
  (->> (str/split (str/trim input) #",")
       (mapv #(Integer/parseInt %))))

(defmulti op-code (fn [_ [op]] op))

(defmethod op-code 1 [nums [_ a b store-at & remaining-prog]]
  [(assoc nums store-at (+ (nums a) (nums b))) remaining-prog])

(defmethod op-code 2 [nums [_ a b store-at & remaining-prog]]
  [(assoc nums store-at (* (nums a) (nums b))) remaining-prog])

(defmethod op-code 99 [nums _]
  [nums nil])

(defn compute
  ([nums] (compute nums nums))
  ([nums program]
   (if program
     (let [[nums program] (op-code nums program)]
       (recur nums program))
     nums)))

(defn set-initial [a b nums]
  (-> (assoc nums 1 a)
      (assoc 2 b)))

(defn solve-1 [noun verb]
  (->> input
       format-input
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
