(defproject ejnkrfsd "0.1.0"
  :description "A toy"
  :url "https://iovxw.net"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :repositories [["bintray" "http://dl.bintray.com/phxql/maven"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [hiccup "1.0.5"] ; html 模板
                 [compojure "1.4.0"] ; http 路由
                 [javax.servlet/servlet-api "2.5"]
                 [http-kit "2.1.19"] ; http 框架
                 [me.raynes/conch "0.8.0"] ; 命令执行
                 [de.mkammerer/argon2-jvm "1.1"] ; password hashing
                 [org.clojure/data.json "0.2.6"] ; json
                 [org.clojure/java.jdbc "0.3.5"] ; 数据库
                 [org.xerial/sqlite-jdbc "3.7.2"] ; sqlite
                 [org.clojure/tools.logging "0.3.1"]] ; log
  :main ^:skip-aot ejnkrfsd.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
