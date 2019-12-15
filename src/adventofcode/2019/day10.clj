(ns adventofcode.2019.day10
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2019/day10.txt"))

(defn ->astroids [grid]
  (for [y     (range (count grid))
        x     (range (count (first grid)))
        :let  [cell (get-in grid [y x])]
        :when (= cell "#")]
    {:x x :y y}))

(defn format-input [input]
  (->> (str/split (str/trim input) #"\n")
       (mapv #(str/split % #""))))

(defn add-geometry [astroids {x2 :x y2 :y}]
  (for [{x1 :x y1 :y :as astroid} astroids]
    (assoc astroid
           :angle    (- Math/PI (Math/atan2 (- x1 x2) (- y1 y2)))
           :distance (+ (Math/pow (- x2 x1) 2) (Math/pow (- y2 y1) 2)))))

(defn solve-1 []
  (let [astroids (-> (format-input input) ->astroids)]
    (->> (map #(->> (add-geometry astroids %)
                    (group-by :angle)
                    (map (comp first (partial sort-by :distance) val)))
              astroids)
         (sort-by count >)
         first
         count)))

(defn checksum [{:keys [x y]}]
  (+ (* x 100) y))

(defn solve-2 []
  (let [astroids (-> (format-input input) ->astroids)
        laser    {:x 28 :y 29}]
    (->> (add-geometry astroids laser)
         (sort-by :distance)
         (group-by :angle)
         (sort-by key)
         (map (comp first val))
         (drop 199)
         first
         checksum)))
