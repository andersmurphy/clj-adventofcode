(ns adventofcode.2019.day03
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn ->line [string]
  (let [direction (subs string 0 1)
        distance  (subs string 1)]
    {:direction direction
     :distance  (Integer/parseInt distance)}))

(defn key-input [[line-a line-b]]
  {:line-a line-a :line-b line-b})

(defn format-input [input]
  (->> (str/split (str/trim input) #"\n")
       (map #(str/split % #","))
       (map #(map ->line %))
       key-input))

(defn move-in-direction [direction point]
  (let [[axis f] ({"R" [:x inc]
                   "L" [:x dec]
                   "U" [:y inc]
                   "D" [:y dec]}
                  direction)]
    (update point axis f)))

(defn ->points [line]
  (reduce (fn [previous-points
               {:keys [direction distance]}]
            (->> (last previous-points)
                 (iterate (partial move-in-direction direction))
                 (drop 1)
                 (take distance)
                 (into previous-points)))
          [{:x 0 :y 0}]
          line))

(defn lines->points [{:keys [line-a line-b] :as m}]
  (-> (assoc m :line-a (->points line-a))
      (assoc :line-b (->points line-b))))

(defn find-intersections [{:keys [line-a line-b] :as m}]
  (->> (set/intersection (set line-a) (set line-b))
       (assoc m :intersections)))

(defn find-closest-intersection [closest-f input]
  (->> input
       format-input
       lines->points
       find-intersections
       closest-f
       sort
       second))

(def input (slurp "resources/adventofcode/2019/day03.txt"))

(defn abs [x]
  (if (< x 0) (- x) x))

(defn manhattan-distance [{:keys [intersections]}]
  (map (fn [{:keys [x y]}]
         (+ (abs x) (abs y))) intersections))

(defn solve-1 []
  (find-closest-intersection manhattan-distance input))

(defn step-distance [{:keys [intersections line-a line-b]}]
  (map (fn [point] (+ (.indexOf line-a point)
                      (.indexOf line-b point)))
       intersections))

(defn solve-2 []
  (find-closest-intersection step-distance input))
