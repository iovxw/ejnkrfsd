(defproject ejnkrfsd "0.1.0"
  :description "A toy"
  :url "https://iovxw.net"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [hiccup "1.0.5"] ; html 模板
                 [compojure "1.4.0"] ; http 路由
                 [aleph "0.4.1-beta4"] ; http 框架
                 [me.raynes/conch "0.8.0"]] ; 命令执行
  :main ^:skip-aot ejnkrfsd.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
