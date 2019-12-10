(ns adventofcode.2019.day05
  (:require [clojure.string :as str]))

(defn format-input [input]
  (->> (str/split (str/trim input) #",")
       (mapv #(Integer/parseInt %))))

(defn ->int [string]
  (Integer/parseInt string))

(defn split-code [code]
  (let [reverse-code (concat (reverse (seq (str code))) (repeat \0))
        opcode       (->int (apply str (reverse (take 2 reverse-code))))
        modes        (map (comp ->int str) (take 3 (drop 2 reverse-code)))]
    (into [opcode] modes)))

(defn prog->args-f [prog pointer]
  (fn [step] (subvec prog (inc pointer) (+ pointer step))))

(defn arg->mode-f [prog mode]
  (fn [arg]
    (case mode
      0 (prog arg)
      1 arg)))

(defn op-code [{:keys [prog in-f pointer out-f] :as m}]
  (let [[opcode & [mode-a mode-b]] (split-code (prog pointer))
        prog->args                 (prog->args-f prog pointer)
        arg-a->mode                (arg->mode-f prog mode-a)
        arg-b->mode                (arg->mode-f prog mode-b)]
    (case opcode
      99 (assoc m :stop true)
      1  (let [step      4
               [a b adr] (prog->args step)]
           (-> (assoc-in m [:prog adr] (+ (arg-a->mode a)
                                          (arg-b->mode b)))
               (update :pointer + step)))
      2  (let [step      4
               [a b adr] (prog->args step)]
           (-> (assoc-in m [:prog adr] (* (arg-a->mode a)
                                          (arg-b->mode b)))
               (update :pointer + step)))
      3  (let [step  2
               [adr] (prog->args step)]
           (-> (assoc-in m [:prog adr] (in-f))
               (update :pointer + step)))
      4  (let [step  2
               [adr] (prog->args step)]
           (out-f (arg-a->mode adr))
           (update m :pointer + step))
      5  (let [step  3
               [a b] (prog->args step)]
           (if (not (zero? (arg-a->mode a)))
             (assoc m :pointer (arg-b->mode b))
             (update m :pointer + step)))
      6  (let [step  3
               [a b] (prog->args step)]
           (if (zero? (arg-a->mode a))
             (assoc m :pointer (arg-b->mode b))
             (update m :pointer + step)))
      7  (let [step      4
               [a b adr] (prog->args step)]
           (-> (assoc-in m [:prog adr]
                         (if (< (arg-a->mode a)
                                (arg-b->mode b)) 1 0))
               (update :pointer + step)))
      8  (let [step      4
               [a b adr] (prog->args step)]
           (-> (assoc-in m [:prog adr]
                         (if (= (arg-a->mode a)
                                (arg-b->mode b)) 1 0))
               (update :pointer + step))))))

(defn compute [{:keys [stop] :as m}]
  (if-not stop (recur (op-code m)) m))

(def input (slurp "resources/adventofcode/2019/day05.txt"))

(defn solve-1 []
  (let [input (format-input input)
        in    (atom [1])
        out   (atom [])
        in-f  (fn [] (first @in))
        out-f #(swap! out conj %)]
    (compute {:prog input :in-f in-f :out-f out-f :pointer 0})
    (->> @out
         last)))

(defn solve-2 []
  (let [input (format-input input)
        in    (atom [5])
        out   (atom [])
        in-f  (fn [] (first @in))
        out-f #(swap! out conj %)]
    (compute {:prog input :in-f in-f :out-f out-f :pointer 0})
    (->> @out
         last)))
