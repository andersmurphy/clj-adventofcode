(ns adventofcode.2019.day09
  (:require [adventofcode.2019.day05 :as intcode]))

(defn extend-memory [prog]
  (into prog (repeat 10000 0)))

(def input (slurp "resources/adventofcode/2019/day09.txt"))

(defn boost [input-instruction]
  (let [input (-> (intcode/format-input input) extend-memory)
        in    (atom [input-instruction])
        out   (atom [])
        in-f  (fn [] (first @in))
        out-f #(swap! out conj %)]
    (intcode/compute
     {:prog input :in-f in-f :out-f out-f :pointer 0 :relative-base 0})
    @out))

(defn solve-1 []
  (boost 1))

(defn solve-2 []
  (boost 2))
