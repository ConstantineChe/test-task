(ns ^:figwheel-no-load test-task.app
  (:require [test-task.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
