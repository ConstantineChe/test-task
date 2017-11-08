(ns test-task.routes.home
  (:require [test-task.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [ring.middleware.json :refer [wrap-json-response]]
            [clojure.core.async :as async :refer [<!! >!!]]))


(defn home-page []
  (layout/render "home.html"))


(def get-user-fields
  (comp (map (fn [user]
               (select-keys user [:name :email])))
        (map (fn [user]
               (update user :name #(str (:first %) " " (:last %)))))))

(defn users []
  (let [{:keys [error body]} (http/get "https://randomuser.me/api/?incl=name,email&results=150"
                                       {:accept :json})]
    (println "Request 150 users")
    (if error
      {:error (.getMessage error)}
      (->> (json/parse-string body true)
           :results
           (into [] get-user-fields)))))

(defn users-lazy-seq []
  (lazy-seq (concat (users) (users-lazy-seq))))


(def users-chan (async/chan 5))

(async/go-loop [seq (users-lazy-seq)]
  (async/>!! users-chan (take 10 seq))
  (recur (drop 10 seq)))

(defroutes home-routes
           (GET "/" []
             (home-page))
           (GET "/docs" []
             (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                 (response/header "Content-Type" "text/plain; charset=utf-8")))
           (wrap-json-response
             (GET "/users" []
               (<!! users-chan))))

