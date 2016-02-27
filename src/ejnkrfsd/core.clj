(ns ejnkrfsd.core
  (:require [ejnkrfsd.steam :as steam]
            [ejnkrfsd.db :as db]
            [org.httpkit.server :refer :all]
            [compojure.core :refer [defroutes GET POST DELETE ANY context]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [clojure.core.async :refer [go]]
            [me.raynes.conch.low-level :as sh])
  (:gen-class))

(defn hello [req]
  (with-channel req channel
    (send! channel {:status 200
                    :headers {"Content-Type" "text/plain"}
                    :body    "hello"})))

(defn get-token [req]
  (let [params (req :query-params)
        id (get params "id")
        password (get params "password")]
    (if (and id password)
      (if-let [info (db/get-user-info id)]
        (if (db/true-passsword? password (info :password))
          {:status 200
           :headers {"Content-Type" "text/plain"}
           :body    (db/update-token id)}
          {:status 403
           :headers {"Content-Type" "text/plain"}
           :body    "密码错误"})
        {:status 403
         :headers {"Content-Type" "text/plain"}
         :body    "用户不存在"})
      {:status 401
       :headers {"Content-Type" "text/plain"}
       :body    "我拒绝!"})))

(defroutes all-routes
  (GET "/" [] hello)
  (GET "/get-token" [] get-token)
  (route/not-found "404"))

(defn -main [& args]
  (let [config (-> (if-let [cfg-file (first args)]
                     cfg-file
                     "config.json")
                   slurp
                   (json/read-str :key-fn keyword))
        database {:classname   "org.sqlite.JDBC"
                  :subprotocol "sqlite"
                  :subname     (str (config :workdir) "database.db")}]
    (prn config)
    (db/init-db database)
    (run-server (site #'all-routes) {:port (config :port)})))
