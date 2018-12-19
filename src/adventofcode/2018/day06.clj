(ns adventofcode.2018.day06
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.math.numeric-tower :as math]))

(defn data->coordinate-tuples [data]
  (->> (map #(Integer/parseInt %) data)
       (partition 2)
       (reduce (fn [acc [x y]]
                 (conj acc {:x x :y y :id (count acc)})) [])))

(def parsed-data
  (-> (slurp (io/resource "adventofcode/2018/day06.txt"))
      (s/split #", |\n")
      data->coordinate-tuples))

(defn bounds [coords]
  (let [by-x (sort-by :x coords) by-y (sort-by :y coords)]
    {:min-x (-> by-x first :x)
     :max-x (-> by-x last :x)
     :min-y (-> by-y first :y)
     :max-y (-> by-y last :y)}))

(defn squares [{:keys [min-x max-x min-y max-y]}]
  (for [x (range min-x (inc max-x))
        y (range min-y (inc max-y))] {:x x :y y}))

(defn nil-if-equally-far [[{d1 :distance id :id} {d2 :distance}]]
  (if (= d1 d2) nil id))

(defn distances-to-square [coords {sqr-x :x sqr-y :y}]
  (map (fn [{:keys [x y id]}]
         {:id id :distance (+ (math/abs (- x sqr-x))
                              (math/abs (- y sqr-y)))})
       coords))

(defn closest [coords square]
  (some->> (distances-to-square coords square)
           (sort-by :distance)
           nil-if-equally-far
           (assoc square :closest)))

(defn total-distance [coords square]
  (->> (distances-to-square coords square)
       (map :distance)
       (apply +)
       (assoc square :total-distance)))

(defn squares-with [f coords]
  (->> (bounds coords)
       squares
       (keep (partial f coords))))

(defn solve-1 []
  (->> parsed-data
       (squares-with closest)
       (group-by :closest)
       (map (fn [[_ squares]] (count squares)))
       sort last))

(defn solve-2 []
  (->> parsed-data
       (squares-with total-distance)
       (sort-by :total-distance)
       (filter (fn [{:keys [total-distance]}] (> 10000 total-distance)))
       count))
