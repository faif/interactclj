(ns basic-lein-clj.core
  (:require [clojure.string :as str]
            [dynadoc.example :refer [defexample defexamples]]))

(defn get-extension
  "Returns the extension in the given path name."
  [^String path]
  (->> (.lastIndexOf path ".")
       (+ 1)
       (subs path)
       str/lower-case))

(defexample get-extension
  (get-extension "myfile.txt"))

(defexamples clojure.core/conj
  ["Add a name to a vector"
   (conj ["Alice" "Bob"] "Charlie")]
  ["Add a number to a list"
   (conj '(2 3) 1)]
  ["Add a key-val pair to a hash map"
   (conj {:name "Alice"} [:age 30])])

(defexamples clojure.core/interleave
  ["Combine two seqs of unequal length"
   (interleave (repeat "a") [1 2 3])]
  ["Create a map using two vectors"
   (apply assoc {}
          (interleave [:fruit :color :temp]
                      ["grape" "red" "hot"]))])

(defexample clojure.core/take-while
  ["Get all the negative numbers up to the first non-negative"]
   (take-while neg? [-2 -1 0 -1 -2 3]))
