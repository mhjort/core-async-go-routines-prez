(ns core-async-demo.core
  (require [clojure.core.async :as async :refer [<! <!! >! >!! chan go go-loop]]))

(defn ping []
  (let [c (chan)]
    (go (>! c "ping"))
  (<!! c)))

(defn divide [dividend divisor]
  (let [c (chan)]
    (go (>! c (double (/ dividend divisor))))
    c))

;(<!! (divide 5 2)) ;2.5
;(<!! (divider 3 0)) ;Blocks forever

(defn launch-logger [input-channel]
  (go-loop []
           (spit "demo.log" (<! input-channel) :append true)
           (spit "rounds.log" "." :append true)
           (recur)))

(def channel (chan))

;(launch-logger channel)
;(>!! channel "ping")

;demo.log contains "ping" and rounds.log contains "."

(async/close! channel) ;rounds.log grows forever

(def input (chan))
(def output (chan))

(defn complex-doubler [input-channel output-channel]
  (go
    (let [value (<! input-channel)]
      (>! output-channel (* 2 value)))))

(defn refactored-doubler [input-channel output-channel]
  (let [do-the-doubling #(>! output-channel (* 2 %))]
  (go
    (let [value (<! input-channel)]
      (do-the-doubling value)))))

(refactored-doubler input output)

(go (>! input 2)) ;Assert failed: >! used not in (go ...) block


(defn start-doubler [input-channel output-channel]
  (go
    (let [value (<! input-channel)]
      (>! output-channel (* 2 value)))))


;(dotimes [_ 1025]
;  (start-doubler input output)) ;Assert failed: No more than 1024 pending

;(go (>! input 3))
;(<!! output) ;6
