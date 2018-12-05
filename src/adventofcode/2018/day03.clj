(ns adventofcode.2018.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :refer [intersection]]))

(def parsed-file
  (->> (slurp (io/resource "adventofcode/2018/day03.txt"))
       str/split-lines))

(defn s->claim [s]
  (let [[id x y w h] (->> (re-seq #"\d+" s) (map #(Integer/parseInt %)))
        tiles        (for [x (range x (+ x w)) y (range y (+ y h))]
                       [x y])]
    {:id id :x x :y y :w w :h h :tiles tiles}))

(defn find-overlapping [claims]
  (->> (mapcat :tiles claims)
       frequencies
       (into #{} (comp
                  (filter (fn [[_ v]] (> v 1)))
                  (map first)))))

(defn solve-1 []
  (->> (map s->claim parsed-file)
       find-overlapping
       count))

(defn solve-2 []
  (let [claims      (map s->claim parsed-file)
        overlapping (find-overlapping claims)]
    (some (fn [{:keys [id tiles]}]
            (when (empty? (intersection overlapping (set tiles)))
              id))
          claims)))
