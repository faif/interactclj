(ns basic-lein-cljs.core
  (:require [reagent.core :as r]
            [goog.object :as gobj]
            [dynadoc.core])
  (:require-macros [dynadoc.example :refer [defexample defexamples]])
  (:import goog.net.XhrIo))

(defn clicks
  "Shows the number of times the user clicked the button."
  [button-text]
  (let [state (r/atom {:clicks 0})]
    (fn []
      [:div
       [:p "You clicked " (:clicks @state) " times"]
       [:button {:on-click (fn []
                             (swap! state update :clicks inc))}
        button-text]])))

(defexample clicks
  {:with-card card
   :with-focus [focus [clicks "Click me!"]]}
  (reagent.core/unmount-component-at-node card)
  (reagent.core/render-component focus card)
  nil)

(defn get-lib-version
  "Queries Clojars for the version of the given library, providing it in a
  callback. If it can't find it, the callback receives an error object."
  [lib-name callback]
  (.send XhrIo
         (str "https://clojars.org/api/artifacts/" lib-name)
         (fn [e]
           (callback
            (or (when (.isSuccess (.-target e))
                  (-> e
                      .-target
                      .getResponseText
                      js/JSON.parse
                      (gobj/get "latest_release")))
                (js/Error. (str "Can't find version for: " lib-name)))))
         "GET"))

(defexample get-lib-version
  {:with-callback callback}
  (get-lib-version "dynadoc" callback))


(defexamples cljs.core/apply
  ["Apply a function to a vector of arguments"
   (apply + [40 2])]
  ["Apply a function to a list of arguments"
   (apply - '(50 5 3))]
  ["Apply a function to multiple collections of different types"
   (map (partial apply max) [[1 2 3 4] '(-1 0 1 2)])])

(defexample cljs.core/comp
  ["Combine (compose) filter and count to get the number of vowels"]
  (def count-if (comp count filter))
  (count-if #{\a\e\i\o\u} "clojure"))

(defexamples cljs.core/conj
  ["Add a name to a vector"
   (conj ["Alice" "Bob"] "Charlie")]
  ["Add a number to a list"
   (conj '(2 3) 1)]
  ["Add a key-val pair to a hash map"
   (conj {:name "Alice"} [:age 30])])

(defexample cljs.core/drop-while
  ["Remove all non-vowel characters up to the first vowel"]
  (drop-while (complement #{\a\e\i\o\u}) "clojure"))

(defexample cljs.core/filter
  ["Exclude languages with single character names"]
  (filter #(> (count %) 1) ["Java" "Lisp" "Fortran" "C" "D" "C++"]))

(defexamples cljs.core/interleave
  ["Combine two seqs of unequal length"
   (interleave (repeat "a") [1 2 3])]
  ["Create a map using two vectors"
   (apply assoc {}
          (interleave [:fruit :color :temp]
                      ["grape" "red" "hot"]))])

(defexample cljs.core/merge-with
  ["Combine all map values that have the same key"]
  (merge-with
   concat
   {:rubble ["Barney"], :flintstone ["Fred"]}
   {:rubble ["Betty"], :flintstone ["Wilma"]}
   {:rubble ["Bam-Bam"], :flintstone ["Pebbles"]}))

(defexamples cljs.core/partition
  ["Break a collection into chunks of two"
   (partition 2 [:n1 :n2 :n3 :n4])]
   ["Break a collection into overlapping pairs"
    (partition 2 1 [:n1 :n2 :n3 :n4])])

(defexamples cljs.core/swap!
  ["Atomically swap the value in a vector-atom"
   (swap! (atom [:lisp]) conj :scheme :clojure)]
  ["Atomically swap a value in a map-atom"
   (let [map-atom (atom {:a "A" :b [4 2]})]
     (swap! map-atom assoc :a "B"))]
  ["Atomically map a function over a seq-value in a map-atom"
   (let [map-atom (atom {:a "A" :b [5 3]})]
     (swap! map-atom update :b (partial mapv dec)))]
  ["Atomically apply a function to a value in a map-atom"
   (let [map-atom (atom {:a "A" :c 41})]
     (swap! map-atom update :c inc))]
  ["Atomically update multiple key-values in one swap"
   (let [map-atom            (atom {:a "A" :b [3 1] :c 43})
         update-multiple-kvs (fn [m k1 f1 k2 f2]
                               (let [m1 (assoc  m k1 (f1 (k1 m)))
                                     m2 (assoc m1 k2 (f2 (k2 m1)))]
                                 m2))
         add-bcd             (fn [a-string] (str a-string "BCD"))
         inc-vec             (fn [v] (mapv inc v))]
     (swap! map-atom update-multiple-kvs :a add-bcd :b inc-vec))])

(defexamples cljs.core/sort-by
  ["Sort using a specific keyword"
   (sort-by :year < [{:name "Lisp" :year 1959}
                     {:name "Fortran" :year 1957}
                     {:name "Smalltalk" :year 1972}])]
  ["Sort numbers lexicographically"
   (sort-by str [5 18 83 23 40])]
  ["Sort with a function"
   (sort-by count ["oh" "this" "is" "super" "awesome"])]
  ["Sort using multiple criteria"
   (sort-by (juxt count str) ["oh" "this" "is" "super" "awesome"])]
  ["Sort using multiple criteria"
   (sort-by (juxt :a :b) [{:a 1 :b 3} {:a 1 :b 2} {:a 2 :b 1}])])

(defexample cljs.core/take-while
  ["Get all the negative numbers up to the first non-negative"]
  (take-while neg? [-2 -1 0 -1 -2 3]))
