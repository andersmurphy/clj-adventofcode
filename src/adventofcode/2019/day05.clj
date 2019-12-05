(ns adventofcode.2019.day05
  (:require [clojure.string :as str]))

(defn format-input [input]
  (->> (str/split (str/trim input) #",")
       (mapv #(Integer/parseInt %))))

(defn op-code [{:keys [prog prog-input index] :as m}]
  (let [[code & args] (drop index prog)]
    (({1    (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ (prog a) (prog b)))
                                (update :index + 4)))
       101  (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ a (prog b)))
                                (update :index + 4)))
       1001 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ (prog a) b))
                                (update :index + 4)))
       1101 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ a b))
                                (update :index + 4)))
       2    (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* (prog a) (prog b)))
                                (update :index + 4)))
       102  (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* a (prog b)))
                                (update :index + 4)))
       1002 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* (prog a) b))
                                (update :index + 4)))
       1102 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* a b))
                                (update :index + 4)))
       3    (fn [[adr]]     (-> (assoc-in m [:prog adr] prog-input)
                                (update :index + 2)))
       4    (fn [[adr]]     (-> (update m :prog-output conj (prog adr))
                                (update :index + 2)))
       104  (fn [[adr]]     (-> (update m :prog-output conj  adr)
                                (update :index + 2)))
       99   (fn [_] (assoc m :stop true))}
      code)
     args)))

(defn compute [{:keys [stop] :as m}]
  (if-not stop (recur (op-code m)) m))

(def input (slurp "resources/adventofcode/2019/day05.txt"))

(defn solve-1 []
  (let [input (format-input input)]
    (-> {:prog input :prog-input 1 :prog-output [] :index 0}
        compute)))
