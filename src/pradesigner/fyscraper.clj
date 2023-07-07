(ns pradesigner.fyscraper
  (:gen-class)
  (:require [pradesigner.io :as io]
            [hiccup.core :as hc]
            [tablecloth.api :as tc]))


#_(defn- tsv>html
  "process an exch tsv file into html"
  [exch]
  (let [ds (io/exch-tsv>ds exch)
        htmlpage (yns/mk-html ds)]
    (io/html-write exch htmlpage)))

(defn- exch-ch
  []
  (println "Type in an exchange:")
  (apply pr io/exchs)
  (println)
  (io/tsv>html (read-line)))

(defn- exch-all
  []
  (map io/tsv>html io/exchs))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (prn "Make a selection")
  (prn "1. Do a specific exchange")
  (prn "2. Do all exchanges")
  (case (read-line)
    "1" (exch-ch)
    "2" (exch-all)
    (println "input correctly or go away!")))

(-main)


