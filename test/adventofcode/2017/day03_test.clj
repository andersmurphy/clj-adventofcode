(ns adventofcode.2017.day03-test
  (:require [clojure.test :refer :all]
            [adventofcode.2017.day03 :refer :all]))

(deftest test-solve-manhattan-spiral-steps
  (testing "1 returns 0 steps"
    (is (= (solve-manhattan-spiral-steps 1) 0)))

  (testing "12 returns 3 steps"
    (is (= (solve-manhattan-spiral-steps 12) 3)))

  (testing "23 returns 2 steps"
    (is (= (solve-manhattan-spiral-steps 23) 2)))

  (testing "1024 returns 31 steps"
    (is (= (solve-manhattan-spiral-steps 1024) 31))))
