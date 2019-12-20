(ns adventofcode.2019.day13
  (:require [adventofcode.2019.day05 :as intcode]
            [adventofcode.2019.day09 :as memory]
            [clojure.core.async :as a]))

(def input (slurp "resources/adventofcode/2019/day13.txt"))

(def id->tile {0 :empty 1 :wall 2 :block 3 :paddle 4 :ball})

(defn initial-screen-state [x-size y-size]
  (vec (repeat y-size (vec (repeat x-size :empty)))))

(defn render [screen-state [x y id]]
  (assoc-in screen-state [y x] (id->tile id)))

(defn screen [in]
  (->> (repeatedly #(a/<!! in))
       (take-while identity)
       (partition 3)
       (reduce render (initial-screen-state 100 100))))

(defn computer [in out prog]
  (a/thread (intcode/compute
             {:prog          (memory/extend-memory prog)
              :in-f          #(a/<!! in)
              :out-f         #(a/>!! out %)
              :pointer       0
              :relative-base 0})
            (a/close! in)
            (a/close! out)))

(defn solve-1 []
  (let [in  (a/chan)
        out (a/chan)]
    (->> input
         intcode/format-input
         (computer in out))
    (->> (screen out)
         flatten
         (filter #{:block})
         count)))

(def initial-state
  {:score             0
   :ball-x-position   0
   :paddle-x-position 0})

(defn joystick [{:keys [ball-x-position paddle-x-position]}]
  (cond (> ball-x-position paddle-x-position) 1
        (< ball-x-position paddle-x-position) -1
        :else                                 0))

(defn track [state out [x y id]]
  (cond (= [x y][-1 0]) (assoc state :score id)
        (= id 4)        (let [state (assoc state :ball-x-position x)]
                          (a/>!! out (joystick state))
                          state)
        (= id 3)        (assoc state :paddle-x-position x)
        :else           state))

(defn auto-pilot [in out]
  (loop [state initial-state]
    (if-let [draw-data
             (seq (remove nil? (repeatedly 3 #(a/<!! in))))]
      (recur (track state out draw-data))
      state)))

(defn insert-quarters [prog]
  (assoc prog 0 2))

(defn solve-2 []
  (let [computer-in  (a/chan 1)
        computer-out (a/chan 1)]
    (->> input
         intcode/format-input
         insert-quarters
         (computer computer-in computer-out))
    (auto-pilot computer-out computer-in)))
