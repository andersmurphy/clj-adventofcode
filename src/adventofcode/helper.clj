(ns adventofcode.helper
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clj-http.client :as client])
  (:import [java.io FileNotFoundException]))

(defonce session-token
  (-> (slurp "secrets.edn")
      edn/read-string
      :session-token))

(defn add-leading-zeros-if-needed [day]
  (if (= (count day) 1)
    (str "0" day)
    day))

(defn ->local-input-path [year day]
  (str "resources/adventofcode/" year "/day"
       (add-leading-zeros-if-needed day)  ".txt"))

(defn ->site-input-path [year day]
  (str "https://adventofcode.com/" year "/day/" day "/input"))

(defn ->local-src-path [year day]
  (str "src/adventofcode/" year "/day"
       (add-leading-zeros-if-needed day) ".clj"))

(defn file-template [year day]
  (str `(~'ns ~(symbol (str "adventofcode." year ".day"
                            (add-leading-zeros-if-needed day))))
       "\n"
       "\n"
       `(~'def ~'input (~'slurp ~(->local-input-path year day)))))

(defn write-file-with-missing-directories! [path content]
  (io/make-parents path)
  (spit path content))

(defn get-input-data! [year day]
  (->> (client/get
        (->site-input-path year day)
        {:headers {:cookie (str "session=" session-token)}})
       :body
       (write-file-with-missing-directories!
        (->local-input-path year day))))

(defn create-file-for-puzzle! [year day]
  (let [year (str year)
        day  (str day)]
    (get-input-data! year day)
    (->> (file-template year day)
         (write-file-with-missing-directories!
          (->local-src-path year day)))))

(comment (create-file-for-puzzle! 2019 1))
