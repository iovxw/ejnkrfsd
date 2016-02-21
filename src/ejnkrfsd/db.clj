(ns ejnkrfsd.db
  (:require [clojure.java.jdbc :as jdbc])
  (:import (de.mkammerer.argon2 Argon2Factory)))

(def argon2 (Argon2Factory/create))

(defn init-db [db]
  (jdbc/execute! db ["CREATE TABLE IF NOT EXISTS user (
                      id          VARCHAR,
                      password    VARCHAR)"]))

(defn new-user [db id password]
  (let [password-hash (.hash argon2 2 65536 1 password)]
    (jdbc/insert! db :user {:id id, :password password-hash})))

(defn query-first [db query]
  (first (jdbc/query db query)))

(defn true-passsword? [db id password]
  (if-let [row (query-first db ["SELECT password FROM user WHERE id = ?" id])]
    (let [password-hash (row :password)]
      (if (.verify argon2 password-hash password)
        true
        false))
    ; (throw Exception. (format "User does not exist: %s" id))
    ; 用户不存在，不过为了防止有人猜用户名，所以不提示
    false))
