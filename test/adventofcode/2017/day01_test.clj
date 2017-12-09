(ns adventofcode.2017.day01-test
  (:require [clojure.test :refer :all]
            [adventofcode.2017.day01 :refer :all]))

(deftest test-solve-captcha

  (testing "returns 0 when input is an empty string"
    (is (= (solve-captcha "") 0)))

  (testing "1122 returns a sum of 3"
    (is (= (solve-captcha "1122") 3)))

  (testing "1111 returns a sum of 4"
    (is (= (solve-captcha "1111") 4)))

  (testing "1234 returns a sum of 0"
    (is (= (solve-captcha "1234") 0)))

  (testing "91212129 returns a sum of 9"
    (is (= (solve-captcha "91212129") 9))))
