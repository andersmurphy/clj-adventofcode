(ns adventofcode.2019.day10
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day10.txt"))

(defn ->astroids [grid]
  (set (for [y     (range (count grid))
             x     (range (count (first grid)))
             :let  [cell (get-in grid [y x])]
             :when (= cell "#")]
         {:x x :y y})))

(defn format-input [input]
  (->> (str/split (str/trim input) #"\n")
       (mapv #(str/split % #""))))

(defn line-of-sight [astroids {x2 :x y2 :y :as point}]
  (-> (for [{x1 :x y1 :y} (disj astroids point)]
        (Math/atan2 (- y1 y2) (- x1 x2)))
      set
      count))

(defn solve-1 []
  (let [astroids (-> (format-input input) ->astroids)]
    (->> (map (partial line-of-sight astroids) astroids)
         (apply max))))

(comment (time (solve-1)) )


(defn solve-2 []
  (->> input
       format-input))
