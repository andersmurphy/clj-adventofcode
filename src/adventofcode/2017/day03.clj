(ns adventofcode.2017.day03
  (:require [clojure.math.numeric-tower :as math]))

(defn find-bottom-corner-root [number]
  (let [root (int (math/ceil (math/sqrt number)))]
    (if (even? root)
      (inc root)
      root)))

(defn bottom-right-corner-value [number]
  (* number number))

(defn number-coordinates [number]
  (let [root (find-bottom-corner-root number)
        bottom-right (bottom-right-corner-value root)
        bottom-left (- bottom-right (dec root))
        top-left (- bottom-left (dec root))
        top-right (- top-left (dec root))
        entry-bottom-right (- top-right (- root 2))]
    (cond
      (<= bottom-left number bottom-right)
      [(- number bottom-left) (dec root)]
      (<= top-left number bottom-left)
      [0 (- number top-left)]
      (<= top-right number top-left)
      [(- top-left number) 0]
      (<= entry-bottom-right number top-right)
      [(dec root) (inc (- number entry-bottom-right))])))

(defn zero-coordinates [number]
  (let [root (find-bottom-corner-root number)
        half-root (int (/ root 2))]
    [half-root half-root]))

(defn solve-manhattan-spiral-steps [number]
  (let [[x1 y1] (zero-coordinates number)
        [x2 y2] (number-coordinates number)]
    (+ (math/abs (- x1 x2)) (math/abs (- y1 y2)))))

;; (solve-manhattan-spiral-steps 277678)
