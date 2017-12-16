(ns adventofcode.2017.day05-test
  (:require [clojure.test :refer :all]
            [adventofcode.2017.day05 :refer :all]))

(deftest test-solve-maze
  (testing "escapes maze in 5 steps"
    (is (= (solve-maze "0\n3\n0\n1\n-3") 5))))
