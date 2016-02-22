(ns ejnkrfsd.db
  (:require [clojure.java.jdbc :as jdbc])
  (:import (de.mkammerer.argon2 Argon2Factory)))

(def argon2 (Argon2Factory/create))

(defn init-db [db]
  (jdbc/execute! db ["CREATE TABLE IF NOT EXISTS user (
                      id          VARCHAR,
                      password    VARCHAR,
                      admin       BOOLEAN)"]))

(defn new-user [db id password]
  (let [password-hash (.hash argon2 2 65536 1 password)]
    (jdbc/insert! db :user {:id id, :password password-hash})))

(defn get-user-info [db id]
  (-> (jdbc/query db ["SELECT * FROM user WHERE id = ? LIMIT 1" id])
      first))

(defn true-passsword? [password password-hash]
  (.verify argon2 password-hash password))
