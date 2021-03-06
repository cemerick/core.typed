(ns clojure.core.typed.test.kw-args-undeclared-fail
  (:require [clojure.core.typed :refer [ann check-ns ann-form cf]]))

;; invoking a kw-fn with undeclared keywords is an error
(ann undeclared-kw-invoke-test [& :optional {:foo Any} -> nil])
(defn undeclared-kw-invoke-test [& a])

(undeclared-kw-invoke-test :blah 'a)
