(ns clojure.core.typed.test.records2
  (:require [clojure.core.typed :as t :refer [ann ann-form cf ann-record]]))

(ann-record Point [lat :- Number
                   lng :- Number])
(defrecord Point [lat lng])

(ann-record VerticalLine [p :- Point
                          l :- Number])
(defrecord VerticalLine [p l])

(ann-record HorizontalLine [p :- Point
                            l :- Number])
(defrecord HorizontalLine [p l])

(ann-record Rect [l :- VerticalLine
                  r :- VerticalLine
                  t :- HorizontalLine
                  b :- HorizontalLine])
(defrecord Rect [^VerticalLine l
                 ^VerticalLine r
                 ^HorizontalLine t
                 ^HorizontalLine b])

(ann hline [Double Double Double -> HorizontalLine])
(defn hline [lat lng l]
  (HorizontalLine. (Point. lat lng) l))

(ann vline [Double Double Double -> VerticalLine])
(defn vline [lat lng l]
  (VerticalLine. (Point. lat lng) l))

(ann create-rect [Double Double Double Double -> Rect])
(defn create-rect [lat_ll lng_ll lat_ur lng_ur]
  (let [ll (Point. lat_ll lng_ll)
        ul (Point. lat_ur lng_ll)
        ur (Point. lat_ur lng_ur)
        lr (Point. lat_ll lng_ur)
        lh (- lng_ur lng_ll)
        lv (- lat_ur lat_ll)]
    (Rect. (VerticalLine. ll lv)
           (VerticalLine. lr lv)
           (HorizontalLine. ul lh)
           (HorizontalLine. ll lh))))

(ann lines-intersect? [VerticalLine HorizontalLine -> Boolean])
(defn lines-intersect? [v h]
  (and
   (>= (:lat (:p h)) (:lat (:p v)))
   (>= (:lng (:p v)) (:lng (:p h)))
   (<= (:lat (:p h)) (+ (:lat (:p v)) (:l v)))
   (<= (:lng (:p v)) (+ (:lng (:p h)) (:l h)))))

(ann rect-overlap? [Rect Rect -> Boolean])
(defn rect-overlap? [r0 r1]
  (or
    (lines-intersect? (:l r0) (:t r1))
    (lines-intersect? (:l r0) (:b r1))
    (lines-intersect? (:r r0) (:t r1))
    (lines-intersect? (:r r0) (:b r1))
    (lines-intersect? (:l r1) (:t r0))
    (lines-intersect? (:l r1) (:b r0))
    (lines-intersect? (:r r1) (:t r0))
    (lines-intersect? (:r r1) (:b r0))))