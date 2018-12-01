(ns core-async-demo.core
  (require [clojure.core.async :as async :refer :all]))


(defn ping []
  (let [c (chan)]
    (go (>! c "ping"))
  (<!! c))
