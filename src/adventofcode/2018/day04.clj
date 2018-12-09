(ns adventofcode.2018.day04
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn data->m [s]
  (let [[_ time _ id event]
        (re-find #"\[(.*?)\] (Guard #(\d+))* *(\w+ \w+)" s)]
    {:time time :id id :event event}))

(def parsed-data
  (->> (slurp (io/resource "adventofcode/2018/day04.txt"))
       str/split-lines (map data->m)))

(defn populate-missing-ids [entries]
  (reduce (fn [entries {:keys [id] :as entry}]
            (let [id (or id (:id (last entries)))]
              (conj entries (assoc entry :id id))))[] entries))

(defn begins-shift-entry? [{:keys [event]}] (= event "begins shift"))

(defn split-time [{:keys [time] :as entry}]
  (let [[date time]   (str/split time #" ")
        [_ time-mins] (str/split time #":")]
    (assoc entry :date date :time (Integer/parseInt time-mins))))

(defn merge-pairs [[sleep awake]]
  (update sleep :time range (awake :time)))

(defn merge-time [[id entries]]
  (reduce (fn [minutes {:keys [time]}]
            (update minutes :time into time)) {:id id} entries))

(defn add-total-minutes [{:keys [time] :as entry}]
  (assoc entry :total-minutes (count time)))

(defn add-most-likely-minute-to-be-asleep [{:keys [time] :as entry}]
  (let [[minute freq] (->> (frequencies time) (sort-by second) last)]
    (assoc entry :minute-most-asleep minute :freq-most-asleep freq)))

(defn id*minute-most-asleep [{:keys [id minute-most-asleep]}]
  (* (Integer/parseInt id) minute-most-asleep))

(defn find-entry-with [sort-key]
  (->> parsed-data
       (sort-by :time)
       populate-missing-ids
       (remove begins-shift-entry?)
       (map split-time)
       (partition 2)
       (map merge-pairs)
       (group-by :id)
       (map merge-time)
       (map add-total-minutes)
       (map add-most-likely-minute-to-be-asleep)
       (sort-by sort-key) last
       id*minute-most-asleep))

(defn solve-1 [] (find-entry-with :minute-most-asleep))

(defn solve-2 [] (find-entry-with :freq-most-asleep))
