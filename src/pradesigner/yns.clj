(ns pradesigner.yns
  "builds ds containing symbol, company, profile with ays"
  (:require [pradesigner.fy :as fy :refer [fy]]
            [pradesigner.io :as io]
            [tablecloth.api :as tc]
            [hiccup.core :as hc]
            [clojure.string :as s]))


(defn- join-regex
  "joins regex strings: patterns -> str -> re-pattern"
  [& patterns]
  (re-pattern (apply str
                     (map #(str %) patterns))))

(defn- kappear
  "counts how many times a word appears in a string"
  [word string]
  (count (re-seq (re-pattern (join-regex "\\b" "(?i)" word "\\b"))
                 string))
  )

(defn- bgcoloring
  "replaces word in string with html for background color"
  [string word color]
  (s/replace string
             (join-regex "\\b" "(?i)" "(" word ")" "\\b")
             (hc/html [:span
                       {:style
                        (str "background-color:" color)} "$1"])))

(defn- yn-sum
  "counts up total appearences of ay in string"
  [string yns]
  (apply + (map #(kappear % string) yns)))

(defn- yn-color
  "css colors a profile string with yn"
  [string yns color]
  (loop [txt string
         yn (set yns)]
    (if (empty? yn)
      txt
      (recur
       (bgcoloring txt (first yn) color)
       (rest yn)))))

(defn- ds-fill
  "sets up the dataset with all columns"
  [dataset]
  (as-> dataset ds
    (tc/add-or-replace-column ds :yayk (map #(yn-sum % io/yays) (ds :profile)))
    (tc/add-or-replace-column ds :nayk (map #(yn-sum % io/nays) (ds :profile)))
    (tc/add-or-replace-column ds :profile (map #(yn-color % io/yays "#90EE90") (ds :profile)))
    (tc/add-or-replace-column ds :profile (map #(yn-color % io/nays "#FFB6C1") (ds :profile)))
    (tc/order-by ds :yayk :desc)))


;; API

(defn mk-html
  "creates the html page from ds"
  [dataset]
  (hc/html [:table {:border 1 :style "text-align:center"}
            (for [[symb comp prof yayk nayk] (tc/rows (ds-fill dataset))]
              (conj [:tr
                     [:td [:a {:href (str fy symb)} symb]]
                     [:td comp]
                     [:td yayk]
                     [:td nayk]
                     [:td prof]]))])) 

