(ns adventofcode.2019.day03
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (slurp "resources/adventofcode/2019/day03.txt"))

(defn ->displacement [string]
  (let [direction (subs string 0 1)
        distance  (subs string 1)]
    {:direction direction
     :distance  (Integer/parseInt distance)}))

(defn format-input [input]
  (->> (str/split (str/trim input) #"\n")
       (map #(str/split % #","))
       (map #(map ->displacement %))))

(defn move-in-direction [direction point]
  (let [[axis f] ({"R" [:x inc]
                   "L" [:x dec]
                   "U" [:y inc]
                   "D" [:y dec]}
                  direction)]
    (-> (update-in point [:pos axis] f)
        (update :steps inc))))

(defn generate-points [displacements]
  (reduce (fn [previous-points
               {:keys [direction distance]}]
            (->> (last previous-points)
                 (iterate (partial move-in-direction direction))
                 (drop 1)
                 (take distance)
                 (into previous-points)))
          [{:pos   {:x 0 :y 0}
            :steps 0}]
          displacements))

(defn find-intersections [[line-a line-b]]
  (let [line-a-points (generate-points line-a)
        line-b-points (generate-points line-b)
        intersections (set/intersection (set (map :pos line-a-points))
                                        (set (map :pos line-b-points)))]
    (->> (filter #(-> % :pos intersections) (into line-a-points line-b-points))
         (group-by :pos)
         (map (fn [[_ [point {:keys [steps]}]]]
                (update point :steps + steps))))))

(defn abs [x]
  (if (< x 0) (- x) x))

(defn find-closest-by-manhattan [points]
  (->> (map (fn [{{:keys [x y]} :pos}]
              (+ (abs x) (abs y))) points)
       sort
       second))

(defn solve-1 []
  (->> input
       format-input
       find-intersections
       find-closest-by-manhattan))

(defn find-closest-by-step [points]
  (->> (map (fn [{:keys [steps]}] steps) points)
       sort
       second))

(defn solve-2 []
  (->> input
       format-input
       find-intersections
       find-closest-by-step))
