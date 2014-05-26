(ns clojure.core.typed.test.parse-unparse-test
  (:require 
    [clojure.core.typed.test.test-utils :refer :all]
    [clojure.test :refer :all]
    [clojure.core.typed.parse-unparse :refer :all]
    [clojure.core.typed :as t]))

(deftest unparse-free-scoping-test
  (is-clj (= (second
               (unparse-type 
                 (parse-type 
                   `(t/All ~'[a b] (t/FnCase [t/Any t/Any ~'-> t/Any])))))
             '[a b]))
  (is-clj (= (clj (-> (unparse-type (parse-type `(t/TFn ~'[[a :variance :covariant]] ~'a)))
                      next)
             (quote ([[a :variance :covariant]] a)))))
  (is-clj (= '(All [a b] (Fn [Any Any -> (Fn [a b -> nil :filters {:then ff :else tt}])
                             :filters {:then tt :else ff}]))
             (->
               (tc-e
                 (fn [f coll]
                   (fn
                     [x :- a
                      y :- b]))
                 :expected
                 (All [a b] [Any Any -> Any]))
               :t
               unparse-type))))
