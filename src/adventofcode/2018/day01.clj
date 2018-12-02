(ns adventofcode.2018.day01
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-file []
  (->> (slurp (io/resource "adventofcode/2018/day01.txt"))
       str/split-lines
       (map #(Integer/parseInt %))))

(defn solve-1 []
  (->> (parse-file)
       (apply +)))

(defn find-first-dup
  ([frequencies]
   (find-first-dup [#{} 0] (cycle frequencies)))
  ([[seen old-freq] infseq]
   (let [new-freq (+ old-freq (first infseq))]
     (if (seen new-freq)
       new-freq
       (recur [(conj seen new-freq) new-freq] (drop 1 infseq))))))

(defn solve-2 []
  (-> (parse-file)
      find-first-dup))
