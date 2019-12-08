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

(defn system-1 [input [a b c d e]]
  (let [a-chan   (a/chan)
        b-chan   (a/chan)
        c-chan   (a/chan)
        d-chan   (a/chan)
        e-chan   (a/chan)
        out-chan (a/chan)]
    (a/go (a/>! a-chan a)
          (a/>! b-chan b)
          (a/>! c-chan c)
          (a/>! d-chan d)
          (a/>! e-chan e)
          (a/>! a-chan 0))
    (a/go (intcode/compute
           {:prog input :in-chan a-chan :out-chan b-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan b-chan :out-chan c-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan c-chan :out-chan d-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan d-chan :out-chan e-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan e-chan :out-chan out-chan :pointer 0}))
    (->> (repeatedly #(a/<!! out-chan))
         (take-while identity)
         last)))

(defn solve-1 []
  (let [input (intcode/format-input input)]
    (->> (permutations [0 1 2 3 4])
         (map #(system-1 input %))
         (sort >)
         first)))

(defn system-2 [input [a b c d e]]
  (let [a-chan     (a/chan)
        b-chan     (a/chan)
        c-chan     (a/chan)
        d-chan     (a/chan)
        e-chan     (a/chan)
        log-chan   (a/chan)
        out-chan   (a/chan)
        multi-chan (a/mult a-chan)]
    (a/tap multi-chan log-chan)
    (a/tap multi-chan out-chan)
    (a/go
      (a/>! a-chan a)
      (a/>! b-chan b)
      (a/>! c-chan c)
      (a/>! d-chan d)
      (a/>! e-chan e)
      (a/>! a-chan 0))
    (a/go (intcode/compute
           {:prog input :in-chan out-chan :out-chan b-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan b-chan :out-chan c-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan c-chan :out-chan d-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan d-chan :out-chan e-chan :pointer 0}))
    (a/go (intcode/compute
           {:prog input :in-chan e-chan :out-chan a-chan :pointer 0}))
    (->> (repeatedly #(a/<!! log-chan))
         (take-while identity)
         last)))

(defn solve-2 []
  (let [input (intcode/format-input input)]
    (->> (permutations [5 6 7 8 9])
         (map #(system-2 input %))
         (sort >)
         first)))
