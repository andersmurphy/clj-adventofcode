(ns adventofcode.2019.day06
  (:require [clojure.string :as str]))

(defn format-input [input]
  (->> (str/split (str/trim input) #"\n")
       (map str/trim)
       (map #(str/split % #"\)"))))

(defn connected-nodes [n edges]
  (->> (filter (fn [[x _]] (= x n)) edges)
       (map second)))

(defn build-graph
  ([edges] (build-graph edges "COM"))
  ([edges start-node]
   (when-let [nodes (connected-nodes start-node edges)]
     [start-node  (mapv (partial build-graph edges) nodes)])))

(defn build-path-list [graph]
  (->> (tree-seq second second graph)
       (map flatten)))

(defn total-orbits [path-list]
  (->>(map #(-> % count dec) path-list)
      (reduce +)))

(def input (slurp "resources/adventofcode/2019/day06.txt"))

(defn solve-1 []
  (->> input
       format-input
       build-graph
       build-path-list
       total-orbits))

(defn length-of-longest-path-containing-a-and-not-b [a b paths]
  (->> (map set paths)
       (filter #(% a))
       (remove #(% b))
       (sort-by >)
       count
       dec))

(defn distance-between [node-a node-b path-list]
  (+ (length-of-longest-path-containing-a-and-not-b
      node-a node-b path-list)
     (length-of-longest-path-containing-a-and-not-b
      node-b node-a path-list)))

(defn solve-2 []
  (->> input
       format-input
       build-graph
       build-path-list
       (distance-between "YOU" "SAN")))
