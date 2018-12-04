(ns adventofcode.2018.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :refer [intersection]]))

(def parsed-file
  (->> (slurp (io/resource "adventofcode/2018/day03.txt"))
       str/split-lines))

(defn data-str->numbers [s]
  (let [[id x y w h] (->> (re-seq #"\d+" s) (map #(Integer/parseInt %)))]
    {:id id :x x :y y :w w :h h}))

(defn numbers->tiles [{:keys [id x y w h] :as m}]
  (for [x (range x (+ x w)) y (range y (+ y h))]
    [x y]))

(defn solve-1 []
  (->> parsed-file
       (map data-str->numbers)
       (mapcat numbers->tiles)
       frequencies
       vals
       (filter #(> % 1))
       count))

(defn solve-2 []
  nil)
