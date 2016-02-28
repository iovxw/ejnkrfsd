(ns ejnkrfsd.db
  (:require [clojure.java.jdbc :as jdbc]
            [crypto.random :as random])
  (:import (de.mkammerer.argon2 Argon2Factory)))

(def database (atom {}))
(def argon2 (Argon2Factory/create))

(defn init-db [db]
  (reset! database db)
  (jdbc/execute! db ["CREATE TABLE IF NOT EXISTS user (
                      id          VARCHAR NOT NULL,
                      password    VARCHAR NOT NULL,
                      token       VARCHAR NOT NULL,
                      admin       BOOLEAN DEFAULT FALSE)"]))

(defn gen-token []
  (random/url-part 64))

(defn new-user
  ([id password] (new-user @database id password))
  ([db id password]
   (let [password-hash (.hash argon2 2 65536 1 password)
         token (gen-token)]
     (jdbc/insert! db :user {:id id, :password password-hash, :token token})
     token)))

(defn get-user-info
  ([id] (get-user-info @database id))
  ([db id]
   (-> (jdbc/query db ["SELECT * FROM user WHERE id = ? LIMIT 1" id])
       first)))

(defn true-passsword? [password password-hash]
  (.verify argon2 password-hash password))

(defn update-token
  ([id] (update-token @database id))
  ([db id]
   (let [token (gen-token)]
     (jdbc/execute! db ["UPDATE user SET token = ?" token])
     token)))
