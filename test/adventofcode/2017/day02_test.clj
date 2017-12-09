(ns adventofcode.2017.day02-test
  (:require [clojure.test :refer :all]
            [adventofcode.2017.day02 :refer :all]))

(deftest test-solve-checksum
  (testing "5\t1\t9\t5 returns a sum of 8"
    (is (= (solve-checksum "5\t1\t9\t5") 8)))

  (testing "7\t5\t3 returns a sum of 4"
    (is (= (solve-checksum "7\t5\t3") 4)))

  (testing "2\t4\t6\t8 returns a sum of 0"
    (is (= (solve-checksum "2\t4\t6\t8") 6)))

  (testing "5\t1\t9\t5\n753\n2468 returns a sum of 18"
    (is (= (solve-checksum "5\t1\t9\t5\n7\t5\t3\n2\t4\t6\t8") 18))))
