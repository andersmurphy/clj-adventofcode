(ns adventofcode.2017.day04-test
  (:require [clojure.test :refer :all]
            [adventofcode.2017.day04 :refer :all]))

(deftest test-solve-valid-passphrases
  (testing "aa bb cc dd ee returns 1 valid"
    (is (= (solve-valid-passphrases "aa bb cc dd ee") 1)))

  (testing "aa bb cc dd aa returns 0 valid"
    (is (= (solve-valid-passphrases "aa bb cc dd aa") 0)))

  (testing "aa bb cc dd aaa returns 1 valid"
    (is (= (solve-valid-passphrases "aa bb cc dd aaa") 1)))

  (testing "aa bb cc dd ee
            aa bb cc dd aa
            aa bb cc dd aaa returns 2 valid"
    (is (= (solve-valid-passphrases
            "aa bb cc dd ee\naa bb cc dd aa\naa bb cc dd aaa") 2))))
