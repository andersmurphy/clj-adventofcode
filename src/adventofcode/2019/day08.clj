(ns adventofcode.2019.day08
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day08.txt"))

(defn format-input [input]
  (->> (str/split (str/trim input) #"")
       (map #(Integer/parseInt %))
       (partition (* 25 6))))

(defn checksum [input]
  (let [layer-freq-least-zeros (->> (map frequencies input)
                                    (sort-by #(get % 0))
                                    first)]
    (* (layer-freq-least-zeros 1)
       (layer-freq-least-zeros 2))))

(defn render [image-data]
  (->> (map (fn [line] (map #(if (= % 0) " " 8) line)) image-data)
       (map #(apply str %))))

(defn solve-1 []
  (->> input
       format-input
       checksum))

(defn solve-2 []
  (->> input
       format-input
       (apply map (fn [& args] (some #{1 0} args)))
       (partition 25)
       render))
