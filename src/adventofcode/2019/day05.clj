(ns adventofcode.2019.day05
  (:require [clojure.string :as str]))

(defn format-input [input]
  (->> (str/split (str/trim input) #",")
       (mapv #(Integer/parseInt %))))

(defn code->seq [code]
  (->> (iterate #(quot % 10) code)
       (take 5)
       (map #(mod % 10))))

(defn get-op [{:keys [prog index]}]
  (let [[code] (drop index prog)]
    (->>  (code->seq code)
          (take 2)
          reverse
          (apply str)
          Integer/parseInt)))

(defn get-modes [code]
  (->> (code->seq code)
       (drop 2)))

(defn mode [code prog & args]
  (map (fn [mode param]
         (if (zero? mode) (prog param) param))
       (get-modes code)
       args))

(defmulti op-code get-op)

(defmethod op-code 1 [{:keys [prog index] :as m}]
  (merge m {:prog  (let [[code a b adr] (drop index prog)]
                     (assoc prog adr (apply + (mode code prog a b))))
            :index (+ index 4)}))

(defmethod op-code 2 [{:keys [prog index] :as m}]
  (merge m {:prog  (let [[code a b adr] (drop index prog)]
                     (assoc prog adr (apply * (mode code prog a b))))
            :index (+ index 4)}))

(defmethod op-code 3 [{:keys [prog prog-input index] :as m}]
  (merge m {:prog  (let [[_ adr] (drop index prog)]
                     (assoc prog adr prog-input))
            :index (+ index 2)}))

(defmethod op-code 4 [{:keys [prog-output index prog] :as m}]
  (merge m {:prog-output (let [[code adr] (drop index prog)]
                           (conj prog-output (first (mode code prog adr))))
            :index       (+ index 2)}))

(defmethod op-code 99 [m]
  (assoc m :stop true))

(defn compute [{:keys [stop] :as m}]
  (if-not stop (recur (op-code m)) m))

(def input (slurp "resources/adventofcode/2019/day05.txt"))

(defn solve-1 []
  (let [input (format-input input)]
    (-> {:prog input :prog-input 1 :prog-output [] :index 0}
        compute)))
