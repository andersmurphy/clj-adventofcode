(ns adventofcode.2015.day01
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2015/day01.txt"))

(defn format-input [input]
  (->> (str/trim input)
       (map {\( 1 \) -1})))

(defn solve-1 []
  (->> input
       format-input
       (reduce +)))

(defn solve-2 []
  (->> input
       format-input
       (reduce (fn [{:keys [depth index]} x]
                 (if (> depth -1)
                   {:depth (+ depth x)
                    :index (inc index)}
                   (reduced index)))
               {:depth 0 :index 0})))
