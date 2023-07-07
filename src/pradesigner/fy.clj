(ns pradesigner.fy
  "tools for getting descriptions from fy"
  (:require [hickory.core :as hk]
            [hickory.select :as hks]
            [clj-http.client :as client]))


(def fy "https://finance.yahoo.com/quote/")

(defn- mkurl
  "creates a url on finance.yahoo using stock symbol"
  [sym]
  (str fy sym "/profile"))

(defn- get-html
  "gets the html text from the symbol"
  [sym]
  (try
    (client/get (mkurl sym))
    (catch Exception e (prn (str "ERROR: " (.toString e))))))

(defn- parse-html
  "parses html body to jsoup given the url"
  [sym]
  (loop [txt (get-html sym)]
    (if (not-empty txt)
      (-> txt :body hk/parse hk/as-hickory)
      (do
        (prn "sleeping 6 seconds")
        (Thread/sleep 6000)
        (recur (get-html sym))))))

(defn- get-desc
  "pulls out the description of the stock from the parsed text"
  [ptxt]
  (-> (hks/select (hks/attr :class #(.equals % "Mt(15px) Lh(1.6)"))
                  ptxt)
      first :content first))

(defn- get-title
  "pulls out the description of the stock from the parsed text"
  [ptxt]
  (-> (hks/select (hks/tag :title)
                  ptxt)
      first :content first))


;; API

(defn fy-stock-desc
  "returns the stock description when possible or a blank string"
  [sym]
  (let [ptxt (parse-html sym)
        title (get-title ptxt)
        lookup? (re-find #"^Symbol Lookup" title)]
    (if-not lookup?
      (get-desc ptxt)
      "")))

