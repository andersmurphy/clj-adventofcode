(ns adventofcode.2018.day05
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(def parsed-data
  (-> (slurp (io/resource "adventofcode/2018/day05.txt"))
      s/trim))

(def alphabet "abcdefghijklmnopqrstuvwxyz")

(defn reacts? [a b]
  (and (not= a b)
       (or (= (str a) (s/upper-case b))
           (= (str b) (s/upper-case a)))))

(defn react [units]
  (-> (reduce (fn [acc next-unit]
                (let [last-unit (first acc)]
                  (if (and last-unit (reacts? last-unit next-unit))
                    (drop 1 acc)
                    (conj acc next-unit))))
              '() units)
      count))

(defn solve-1 []
  (-> parsed-data react))

(defn solve-2 []
  (->> (pmap #(-> (s/replace parsed-data
                             (re-pattern (str % "|" (s/upper-case %)))
                             "")
                  react)
             alphabet)
       (apply min)))

;;"Elapsed time: 190.527993 msecs"
