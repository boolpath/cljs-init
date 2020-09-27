(defproject app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :main app.server
  :uberjar-name "app.jar"
  :min-lein-version "2.5.0"

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.4.6"
             :exclusions [org.clojure/clojure]]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773" :scope "provided"]
                 [com.bhauman/figwheel-main "0.2.11"]
                 [reagent "0.10.0" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [reagent-utils "0.3.3"]
                 [hiccup "1.0.5"]
                 [ring "1.8.1"]
                 [ring-server "0.5.0"]
                 [ring/ring-defaults "0.3.2"]
                 [metosin/reitit "0.5.1"]
                 [metosin/jsonista "0.2.6"]
                 [yogthos/config "1.1.7"]
                 [pez/clerk "1.0.0"]
                 [venantius/accountant "0.2.5"
                  :exclusions [org.clojure/tools.reader]]]

  :source-paths ["src/server" "src/common" "src/client"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  [[:css {:source "resources/public/css/site.css"
          :target "resources/public/css/site.min.css"}]]

  :aliases {"fig:run" ["trampoline" "run" "-m" "figwheel.main"]
            "fig:dev" ["fig:run" "--" "--build" "dev" "--repl"]}

  :profiles {:dev {:env {:dev true}
                   :repl-options {:init-ns app.repl}
                   :dependencies [[cider/piggieback "0.5.1"]
                                  [binaryage/devtools "1.0.2"]
                                  [ring/ring-mock "0.4.0"]
                                  [ring/ring-devel "1.8.1"]
                                  [prone "2020-01-17"]
                                  [figwheel-sidecar "0.5.20"]
                                  [nrepl "0.8.0"]
                                  [pjstadig/humane-test-output "0.10.0"]]
                   :source-paths ["env/dev/server"]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]}
             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/server"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}}

  :cljsbuild {:builds {:min
                       {:source-paths ["src/client" "src/common" "env/prod/client"]
                        :compiler
                        {:target :bundle
                         :bundle-cmd {:none ["npx" "webpack" "--mode=development"]
                                      :default ["npx" "webpack" "--mode=production"
                                                "target/cljsbuild/public/js/app-raw.js" "-o"
                                                "target/cljsbuild/public/js/app.js"]}
                         :output-to        "target/cljsbuild/public/js/app-raw.js"
                         :output-dir       "target/cljsbuild/public/js"
                         :source-map       "target/cljsbuild/public/js/app.js.map"
                         :optimizations :advanced
                         :infer-externs true
                         :pretty-print  false}}}})
