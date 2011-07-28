;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns twitterbuzz.dom-helpers
  (:require [goog.dom :as dom]))

(defn append [parent & children]
  (do (doseq [child children]
        (dom/appendChild parent child))
      parent))

(defn element
  "Create a dom element using a keyword for the element name and a map
  for the attributes."
  [tag attrs & children]
  (let [parent (dom/createDom (name tag)
                              (.strobj (reduce (fn [m [k v]]
                                                 (assoc m k v))
                                               {}
                                               (map #(vector (name %1) %2)
                                                    (keys attrs)
                                                    (vals attrs)))))
        [parent children] (if (string? (first children))
                            [(doto (element tag attrs) (dom/setTextContent (first children)))
                             (rest children)]
                            [parent children])]
    (apply append parent children)))

(defn remove-children
  "Remove all children from the element with the passed id."
  [id]
  (let [parent (dom/getElement (name id))]
    (do (dom/removeChildren parent))))

(defn get-element [id]
  (dom/getElement (name id)))

(defn html [s]
  (dom/htmlToDocumentFragment s))

(defn element-arg? [x]
  (or (keyword? x)
      (map? x)
      (string? x)))

(defn build
  "Build up a dom element from nested vectors."
  [x]
  (if (vector? x)
    (let [[parent children] (if (keyword? (first x))
                              [(apply element (take-while element-arg? x))
                               (drop-while element-arg? x)]
                              [(first x) (rest x)])
          children (map build children)]
      (apply append parent children))
    x))

(defn insert-at [parent child index]
  (dom/insertChildAt parent child index))
