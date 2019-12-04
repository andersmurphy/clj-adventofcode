(ns adventofcode.2016.day01
  (:require [clojure.string :as str]))

(def input (slurp "resources/adventofcode/2016/day01.txt"))

(defn format-input [input]
  (->> (str/split (str/trim input) #",")
       (map str/trim)
       (map (fn [x] {:rot   ({"L" :previous "R" :next} (subs x 0 1))
                     :steps (Integer/parseInt (subs x 1))}))))

(def ->heading-data
  {:north {:axis :y :f + :previous :west :next :east :heading :north}
   :east  {:axis :x :f + :previous :north :next :south :heading :east}
   :south {:axis :y :f - :previous :east :next :west :heading :south}
   :west  {:axis :x :f - :previous :south :next :north :heading :west}})

(defn get-final-pos [input]
  (reduce (fn [{:keys [current-heading x y] :as pos} {:keys [rot steps]}]
            (let [{:keys [axis f heading]}
                  (-> current-heading ->heading-data rot ->heading-data)]
              (-> (update pos axis f steps)
                  (assoc :current-heading heading)
                  (update :points into (map (fn [n]
                                              (update {:x x :y y} axis f n))
                                            (range 1 (inc steps)))))))
          {:x 0 :y 0 :current-heading :north :points [{:x 0 :y 0}]} input))

(defn checksum [{:keys [x y]}]
  (+ x y))

(defn solve-1 []
  (->> input
       format-input
       get-final-pos
       checksum))

(defn find-first-dup [{:keys [points]}]
  (reduce (fn [seen point]
            (if (seen point)
              (reduced point)
              (conj seen point)))
          #{}
          points))

(defn solve-2 []
  (->> input
       format-input
       get-final-pos
       find-first-dup
       checksum))
