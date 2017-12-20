(ns adventofcode.2017.day06-test
  (:require [clojure.test :refer :all]
            [adventofcode.2017.day06 :refer :all]))

(deftest test-solve-redistribution-cycles
  (testing "The infinite loop is detected after 5 redistribution cycle"
    (is (= (solve-redistribution-cycles "0\t2\t7\t0") 5))))
