(ns user
  (:require 
            [mount.core :as mount]
            [test-task.figwheel :refer [start-fw stop-fw cljs]]
            test-task.core))

(defn start []
  (mount/start-without #'test-task.core/repl-server))

(defn stop []
  (mount/stop-except #'test-task.core/repl-server))

(defn restart []
  (stop)
  (start))


