(ns test-task.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST json-response-format]]
            [test-task.ajax :refer [load-interceptors!]]
            [test-task.events]
            [clojure.string :as s]
            [cognitect.transit :as t]
            [clojure.walk :refer [keywordize-keys]])
  (:import goog.History))

(def json-reader (t/reader :json))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href     uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
              [:nav.navbar.navbar-dark.bg-primary
               [:button.navbar-toggler.hidden-sm-up
                {:on-click #(swap! collapsed? not)} "☰"]
               [:div.collapse.navbar-toggleable-xs
                (when-not @collapsed? {:class "in"})
                [:a.navbar-brand {:href "#/"} "test-task"]
                [:ul.nav.navbar-nav
                 [nav-link "#/" "Home" :home collapsed?]
                 [nav-link "#/about" "About" :about collapsed?]
                 [nav-link "#/users" "Users" :users collapsed?]
                 [nav-link "#/fizzbazz" "FizzBuzz" :fizzbazz collapsed?]]]]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn home-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])


(defn fizzbazz [n]
  (let [n (str n)]
    (cond-> nil
            (s/includes? n "3") (str "Fizz")
            (s/includes? n "5") (str "Buzz")
            :always (or n))))

(defn fizzbazz-page
  "FizzBazz component"
  []
  [:div.container
   [:div.row
    (->> (range 101)
         (map fizzbazz)
         (map-indexed (fn [i n] [:p {:key i} n]))
         doall)]])

(defn display-user
  "User component"
  [user]
  [:div.col-sm-4 {:style {:border "solid 1px"}}
   [:p "Name: " [:span (:name user)]]
   [:p "Email: " [:span (:email user)]] ])

;; Append users
(defn append-users! []
  (GET "/users" {:handler #(rf/dispatch [:append-users (-> (t/read json-reader %)
                                                           (keywordize-keys))])}))

(defn users-page
  "Users component"
  []
  [:div.container
   [:div.row>div.col-md-4
    [:button
     {:on-click #(append-users!)}
     "More Users"]]
   (when-let [users @(rf/subscribe [:users])]
     [:div.row
      (->> users
           (map-indexed (fn [i user] [:div {:key i} (display-user user)]))
           doall)])])

(def pages
  {:home     #'home-page
   :about    #'about-page
   :fizzbazz #'fizzbazz-page
   :users    #'users-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
                    (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
                    (rf/dispatch [:set-active-page :about]))
;; FizzBazz route
(secretary/defroute "/fizzbazz" []
                    (rf/dispatch [:set-active-page :fizzbazz]))
;; Users route
(secretary/defroute "/users" []
                    (rf/dispatch [:set-active-page :users]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

;; Set initial users
(defn set-users! []
  (GET "/users" {:handler #(rf/dispatch [:set-users (-> (t/read json-reader %)
                                                        (keywordize-keys))])}))


(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (set-users!)
  (hook-browser-navigation!)
  (mount-components))
