(ns pradesigner.io
  "processes io"
  (:require [pradesigner.yns :as yns]
            [tablecloth.api :as tc]))


(defn- file>list
  "creates list from text file"
  [fnam]
  (-> (tc/dataset (str "resources/" fnam ".txt") {:header-row? false})
      (tc/column "column-0")))

(defn- tsv-trim
  "removes excess columns from tsv files (need only once)"
  [exch]
  (let [fpath (str "resources/" exch ".tsv")]
    (-> fpath
        (tc/dataset)
        (tc/select-columns ["symbol" "company" "desc"])
        (tc/rename-columns {"desc" "profile"})
        (tc/write-csv! fpath))))

(defn- exch-txt>ds
  "makes ds from exch.txt files having only symbol and description"
  [fnam]
  (-> (tc/dataset (str "resources/" fnam ".txt"))
      (tc/rename-columns {"Symbol" :symbol
                          "Description" :company})))

(defn- html-write
  "writes html file exch.html given exch and htmlpage"
  [exch htmlpage]
  (spit (str "resources/" exch ".html") htmlpage))

;;TODO
#_(defn- tsv-make
  "makes tsv files from fy probably using exch-txt>ds")


;; API

(def yays (file>list "yays"))
(def nays (file>list "nays"))
(def exchs (file>list "exchs"))

(defn exch-tsv>ds
  "makes ds from exch.tsv file"
  [exch]
  (-> (tc/dataset (str "resources/" exch ".tsv"))
      (tc/rename-columns {"symbol" :symbol
                          "company" :company
                          "profile" :profile})))

(defn tsv>html
  "process an exch tsv file into html"
  [exch]
  (let [ds (exch-tsv>ds exch)
        htmlpage (yns/mk-html ds)]
    (html-write exch htmlpage)))
