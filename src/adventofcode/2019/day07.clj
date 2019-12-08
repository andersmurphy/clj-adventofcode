(ns adventofcode.2019.day07
  (:require [adventofcode.2019.day05 :as intcode]
            [clojure.core.async :as a]))

(def input (slurp "resources/adventofcode/2019/day07.txt"))
(def prog (intcode/format-input input))

(defn permutations [s]
  (lazy-seq
   (if (seq (rest s))
     (for [head s
           tail (permutations (remove #{head} s))]
       (cons head tail))
     [s])))

(defn system-1 [input phases]
  (let [[a b c d e :as channels] (repeatedly #(a/chan))
        out                      (a/chan)]
    (a/go
      (dorun (map (fn [channel phase] (a/>!! channel phase)) channels phases))
      (a/>!! a 0))
    (dorun
     (map (fn [in-chan out-chan]
            (a/go (intcode/compute
                   {:prog input :in-chan in-chan :out-chan out-chan :pointer 0})))
          [a b c d e]
          [b c d e out]))
    (->> (repeatedly #(a/<!! out))
         (take-while identity)
         last)))

(defn solve-1 []
  (let [input (intcode/format-input input)]
    (->> (permutations [0 1 2 3 4])
         (map #(system-1 input %))
         (sort >)
         first)))

(defn system-2 [input phases]
  (let [[a b c d e :as channels] (repeatedly #(a/chan))
        log                      (a/chan)
        out                      (a/chan)
        multi-chan               (a/mult a)]
    (a/tap multi-chan log)
    (a/tap multi-chan out)
    (a/go
      (dorun (map (fn [channel phase] (a/>!! channel phase)) channels phases))
      (a/>!! a 0))
    (dorun
     (map (fn [in-chan out-chan]
            (a/go (intcode/compute
                   {:prog input :in-chan in-chan :out-chan out-chan :pointer 0})))
          [out b c d e]
          [b   c d e a]))
    (->> (repeatedly #(a/<!! log))
         (take-while identity)
         last)))

(defn solve-2 []
  (let [input (intcode/format-input input)]
    (->> (permutations [5 6 7 8 9])
         (map #(system-2 input %))
         (sort >)
         first)))
