(ns test-task.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [test-task.core-test]))

(doo-tests 'test-task.core-test)

