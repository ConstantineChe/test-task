(ns test-task.events
  (:require [test-task.db :as db]
            [re-frame.core :refer [dispatch reg-event-db reg-sub]]))

;;dispatchers

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))


(reg-event-db
  :set-users
  (fn [db [_ users]]
    (assoc db :users users)))

(reg-event-db
  :append-users
  (fn [db [_ users]]
    (update db :users concat users)))

;;subscriptions

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(reg-sub
  :users
  (fn [db _]
    (:users db)))
