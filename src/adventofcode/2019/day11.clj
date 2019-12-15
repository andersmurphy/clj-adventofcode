(ns adventofcode.2019.day11
  (:require [adventofcode.2019.day05 :as intcode]
            [adventofcode.2019.day09 :as memory]
            [clojure.core.async :as a]))

(def input (slurp "resources/adventofcode/2019/day11.txt"))

(defn get-or-create-panel [panels {:keys [x y] :as panel}]
  (or (panels [x y]) (assoc panel :color 0)))

(defn add-panel [panels {:keys [x y] :as panel}]
  (if panel
    (assoc panels [x y] panel :last-panel panel)
    panels))

(defn paint-panel [{:keys [in out]} {:keys [color] :as panel}]
  (a/go (a/>! in color))
  (when-let [color (a/<!! out)]
    (assoc panel :color color)))

(defn add-direction [direction panel]
  (assoc panel :direction direction))

(defn get-direction [out {current-direction :direction}]
  (when-let [direction-code (a/<!! out)]
    ({[0 :north] :west
      [1 :north] :east
      [0 :east]  :north
      [1 :east]  :south
      [0 :south] :east
      [1 :south] :west
      [0 :west]  :south
      [1 :west]  :north} [direction-code current-direction])))

(defn next-panel [{:keys [out result] :as channels}
                  {:keys [last-panel] :as panels}]
  (if-let [direction (get-direction out last-panel)]
    (recur
     channels
     (->> ({:north (update last-panel :y inc)
            :south (update last-panel :y dec)
            :west  (update last-panel :x dec)
            :east  (update last-panel :x inc)}
           direction)
          (get-or-create-panel panels)
          (add-direction direction)
          (paint-panel channels)
          (add-panel panels)))
    (a/>!! result (dissoc panels :last-panel))))

(defn initial-panel [channels start-color]
  (let [first-panel (paint-panel
                     channels
                     {:color start-color :x 0 :y 0 :direction :north})]
    {[0 0]       first-panel
     :last-panel first-panel}))

(defn robot [start-color prog]
  (let [{:keys [in out result] :as channels} {:in     (a/chan)
                                              :out    (a/chan)
                                              :result (a/chan)}]
    (a/go (->> (initial-panel channels start-color)
               (next-panel channels)))
    (a/go (intcode/compute
           {:prog          (memory/extend-memory prog)
            :in-f          #(a/<!! in)
            :out-f         #(a/>!! out %)
            :pointer       0
            :relative-base 0})
          (a/close! in)
          (a/close! out))
    (let [r (a/<!! result)]
      (a/close! result)
      r)))

(defn solve-1 []
  (->> input
       intcode/format-input
       (robot 0)
       count))

(defn plot-graph [points]
  (let [xs       (map :x points)
        min-x    (apply min xs)
        max-x    (apply max xs)
        x-length (inc (- max-x min-x))
        ys       (map :y points)
        min-y    (apply min ys)
        max-y    (apply max ys)
        y-length (inc (- max-y min-y))
        grid     (vec (repeat y-length (vec (repeat x-length " "))))]

    (->> (reduce (fn [grid {:keys [x y]}]
                   (assoc-in grid [(- y min-y) (- x min-x)] "#"))
                 grid points)
         (map println))))

(defn solve-2 []
  (->> input
       intcode/format-input
       (robot 1)
       (map second)
       (filter (comp #{1} :color))
       plot-graph))
