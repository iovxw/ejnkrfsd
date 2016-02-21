(ns ejnkrfsd.core
  (:require [ejnkrfsd.steam :as steam]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [me.raynes.conch.low-level :as sh])
  (:gen-class))

(defn -main [& args]
  (let [config (-> (if-let [cfg-file (first args)]
                     cfg-file
                     "config.json")
                   slurp
                   (json/read-str :key-fn keyword))
        database {:classname   "org.sqlite.JDBC"
                  :subprotocol "sqlite"
                  :subname     (str (config :workdir) "database.db")}]
    (prn config)))
