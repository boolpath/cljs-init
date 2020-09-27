
- lein new reagent app-name
- npm init -y
- npm add react react-dom
- npm add webpack webpack-cli 

- project.clj
```
:dependencies [...
               [com.bhauman/figwheel-main "0.2.11"]
               [reagent "..." :exclusions [cljsjs/react cljsjs/react-dom]]]
:aliases {"fig:run" ["trampoline" "run" "-m" "figwheel.main"]
            "fig:dev" ["fig:run" "--" "--build" "dev" "--repl"]}
:cljsbuild {:builds {:min ... :compiler
    :target :bundle
    :bundle-cmd {:none ["npx" "webpack" "--mode=development"]
               :default ["npx" "webpack" "--mode=production"
                         "target/cljsbuild/public/js/app-raw.js" "-o"
                         "target/cljsbuild/public/js/app.js"]}
    :output-to        "target/cljsbuild/public/js/app-raw.js"
    ...
```

- figwheel-main.edn
```
{:watch-dirs ["src/common" "src/client" "env/dev/client"]
 :css-dirs ["resources/public/css"]
 :ring-handler app.handler/app}
```

- dev.cljs.edn
```
^{:auto-bundle :webpack}
{:main "app.dev"
 :source-map true
 :optimizations :none
 :pretty-print  true
 :bundle-freq :smart
 :closure-defines {cljs.core/*global "window"}}
```

- app.handler.clj (defn loading-page [] ...)
```
(include-js  (if (env :dev) "/cljs-out/dev/main_bundle.js" "/js/app.js"))
```

- app.core.cljs
```
(ns ^:figwheel-hooks app.core ...)
(defn ^:after-load mount-root ...)
```

- Heroku buildpacks (cli commands)
```
heroku buildpacks:remove heroku/clojure
heroku buildpacks:add heroku/nodejs
heroku buildpacks:add heroku/clojure
```