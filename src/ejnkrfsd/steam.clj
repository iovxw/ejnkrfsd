(ns ejnkrfsd.steam
  (:require [clojure.string :as string]
            [clojure.core.async :refer [go]]
            [me.raynes.conch.low-level :as sh]))

(def is-windows (-> (System/getProperty "os.name")
                    .toLowerCase
                    (string/starts-with? "win")))

(defmacro run [& args]
  (let [[cmd opts] (split-with (complement keyword?) args)]
    (if (string/ends-with? (first cmd) ".sh")
      `(sh/proc "bash" "-c" ~(str "\"" (string/join " " cmd) "\"") ~@opts)
      `(sh/proc ~@args))))

(defn update-game [workdir cmd username password]
  (run cmd (format "+login %s %s" username password)
       "+force_install_dir ../unturned"
       (when-not is-windows ; Rocket Linux 版本必须用 32 位
         "+@sSteamCmdForcePlatformBitness 32")
       "+app_update 304930 validate" "+quit" :dir workdir))

(defn update-workshop-item [workdir cmd username password & items]
  (run cmd (format "+login %s %s" username password)
       "+force_install_dir ../workshop"
       (->> items
            (map #(format "+workshop_download_item 304930 %s" %))
            (string/join \space))
       "+quit"
       :dir workdir))
