(ns adventofcode.2018.day06
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.math.numeric-tower :as math]))

(defn data->coordinate-tuples [data]
  (->> (map #(Integer/parseInt %) data)
       (partition 2)
       (reduce (fn [acc [x y]]
                 (conj acc {:x x :y y :id (count acc)})) [])))

(defn parsed-data []
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
  (for [x (range min-x max-x)
        y (range min-y max-y)] {:x x :y y}))

(defn nil-if-equally-far [[{d1 :distance id :id} {d2 :distance}]]
  (if (= d1 d2) nil id))

(defn closest [coords {sqr-x :x sqr-y :y :as m}]
  (some->> (map (fn [{:keys [x y id]}]
                  {:id id :distance (+ (math/abs (- x sqr-x))
                                       (math/abs (- y sqr-y)))})
                coords)
           (sort-by :distance)
           nil-if-equally-far
           (assoc m :closest)))

(defn solve-1 []
  (let [coords (parsed-data)]
    (->> (bounds coords)
         squares
         (keep (partial closest coords))
         (group-by :closest)
         (map (fn [[_ squares]] (count squares)))
         sort last)))
