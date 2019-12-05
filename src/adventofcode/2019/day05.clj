(ns adventofcode.2019.day05
  (:require [clojure.string :as str]))

(defn format-input [input]
  (->> (str/split (str/trim input) #",")
       (mapv #(Integer/parseInt %))))

(defn op-code [{:keys [prog prog-input pointer] :as m}]
  (let [[code & args] (drop pointer prog)]
    (({99   (fn [_] (assoc m :stop true))
       1    (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ (prog a) (prog b)))
                                (update :pointer + 4)))
       101  (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ a (prog b)))
                                (update :pointer + 4)))
       1001 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ (prog a) b))
                                (update :pointer + 4)))
       1101 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (+ a b))
                                (update :pointer + 4)))
       2    (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* (prog a) (prog b)))
                                (update :pointer + 4)))
       102  (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* a (prog b)))
                                (update :pointer + 4)))
       1002 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* (prog a) b))
                                (update :pointer + 4)))
       1102 (fn [[a b adr]] (-> (assoc-in m [:prog adr] (* a b))
                                (update :pointer + 4)))
       3    (fn [[adr]]     (-> (assoc-in m [:prog adr] prog-input)
                                (update :pointer + 2)))
       4    (fn [[adr]]     (-> (update m :prog-output conj (prog adr))
                                (update :pointer + 2)))
       104  (fn [[adr]]     (-> (update m :prog-output conj  adr)
                                (update :pointer + 2)))
       5    (fn [[a b]]    (if (not (zero? (prog a)))
                             (assoc m :pointer (prog b))
                             (update m :pointer + 3)))
       105  (fn [[a b]]    (if (not (zero? a))
                             (assoc m :pointer (prog b))
                             (update m :pointer + 3)))
       1005 (fn [[a b]]    (if (not (zero? (prog a)))
                             (assoc m :pointer b)
                             (update m :pointer + 3)))
       1105 (fn [[a b]]    (if (not (zero? a))
                             (assoc m :pointer b)
                             (update m :pointer + 3)))
       6    (fn [[a b]]    (if (zero? (prog a))
                             (assoc m :pointer (prog b))
                             (update m :pointer + 3)))
       106  (fn [[a b]]    (if (zero? a)
                             (assoc m :pointer (prog b))
                             (update m :pointer + 3)))
       1006 (fn [[a b]]    (if (zero? (prog a))
                             (assoc m :pointer b)
                             (update m :pointer + 3)))
       1106 (fn [[a b]]    (if (zero? a)
                             (assoc m :pointer b)
                             (update m :pointer + 3)))
       7    (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (< (prog a) (prog b)) 1 0))
                                (update :pointer + 4)))
       107  (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (< a (prog b)) 1 0))
                                (update :pointer + 4)))
       1007 (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (< (prog a) b) 1 0))
                                (update :pointer + 4)))
       1107 (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (< a b) 1 0))
                                (update :pointer + 4)))
       8    (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (= (prog a) (prog b)) 1 0))
                                (update :pointer + 4)))
       108  (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (= a (prog b)) 1 0))
                                (update :pointer + 4)))
       1008 (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (= (prog a) b) 1 0))
                                (update :pointer + 4)))
       1108 (fn [[a b adr]] (-> (assoc-in m [:prog adr]
                                          (if (= a b) 1 0))
                                (update :pointer + 4)))}
      code)
     args)))

(defn compute [{:keys [stop] :as m}]
  (if-not stop (recur (op-code m)) m))

(def input (slurp "resources/adventofcode/2019/day05.txt"))

(defn solve-1 []
  (let [input (format-input input)]
    (-> {:prog input :prog-input 1 :prog-output [] :pointer 0}
        compute)))

(defn solve-2 []
  (let [input (format-input input)]
    (-> {:prog input :prog-input 5 :prog-output [] :pointer 0}
        compute)))
