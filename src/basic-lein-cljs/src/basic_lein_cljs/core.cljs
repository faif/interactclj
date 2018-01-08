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

(defexamples cljs.core/interleave
  ["Combine two seqs of unequal length"
   (interleave (repeat "a") [1 2 3])]
  ["Create a map using two vectors"
   (apply assoc {}
          (interleave [:fruit :color :temp]
                      ["grape" "red" "hot"]))])

(defexamples cljs.core/sort-by
  ["Sort using a specific keyword"
  (sort-by :year < [{:name "Lisp" :year 1959}
                    {:name "Fortran" :year 1957}
                    {:name "Smalltalk" :year 1972}])]
  ["Sort numbers lexicographically"
   (sort-by #(.toString %) [5 18 83 23 40])])

(defexample cljs.core/take-while
  ["Get all the negative numbers up to the first non-negative"]
  (take-while neg? [-2 -1 0 -1 -2 3]))
