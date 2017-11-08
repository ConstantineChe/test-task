(ns test-task.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[test-task started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[test-task has shut down successfully]=-"))
   :middleware identity})
