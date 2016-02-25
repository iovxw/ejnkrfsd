(ns ejnkrfsd.core
  (:require [ejnkrfsd.steam :as steam]
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

(defroutes all-routes
  (GET "/" [] hello)
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
    (run-server (site #'all-routes) {:port (config :port)})))
