(ns adventofcode.2019.day12
  (:require [clojure.string :as str]
            [clojure.core.async :as a]))

(defn format-input [input]
  (->> (str/split (str/trim input) #"\n")
       (map (fn [moon]
              (let [pos (->> (re-seq #"-?[0-9]{1,2}" moon)
                             (mapv #(Integer/parseInt %)))]
                {:pos pos
                 :vel [0 0 0]})))
       set))

(defn gravity [a b]
  (cond (> a b) -1
        (< a b) 1
        :else   0))

(defn update-velocity-of-moon
  [{:keys [pos vel] :as moon} other-moons]
  (assoc moon :vel
         (apply mapv +
                vel
                (map (comp (partial map gravity pos) :pos)
                     other-moons))))

(defn update-position-of-moon [{:keys [pos vel] :as moon}]
  (assoc moon :pos (mapv + pos vel)))

(defn update-moons [moons]
  (set (map (fn [moon]
              (-> (update-velocity-of-moon
                   moon
                   (disj moons moon))
                  update-position-of-moon))
            moons)))

(defn abs [n] (max n (- n)))

(defn calculate-energy [moons]
  (->> (map (fn [{:keys [vel pos]}]
              (* (reduce + (map abs vel))
                 (reduce + (map abs pos)))) moons)
       (reduce +)))

(def input (slurp "resources/adventofcode/2019/day12.txt"))

(defn solve-1 []
  (->> input
       format-input
       (iterate update-moons)
       (drop 1000)
       first
       calculate-energy))

(defn steps-for-axis-to-align
  ([moons axis]
   (steps-for-axis-to-align
    moons axis 0
    (set (map #(get-in % [:pos axis]) moons))
    (set (map #(get-in % [:vel axis]) moons))))
  ([moons axis step init-pos init-vel]
   (if (and
        (not= step 0)
        (= init-pos (set (map #(get-in % [:pos axis]) moons)))
        (= init-vel (set (map #(get-in % [:vel axis]) moons))))
     step
     (recur (update-moons moons) axis (inc step) init-pos init-vel))))

(defn gcd [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))

(defn lcm [& args]
  (reduce (fn [a b] (/ (* a b) (gcd a b))) args))

(defn solve-2 []
  (let [moons (format-input input)
        x     (a/go (steps-for-axis-to-align moons 0))
        y     (a/go (steps-for-axis-to-align moons 1))
        z     (a/go (steps-for-axis-to-align moons 2))]
    (lcm (a/<!! x) (a/<!! y) (a/<!! z))))
