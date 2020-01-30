## Help

* The command of debug processor

```
./gradlew compileDebugJavaWithJavac -Dorg.gradle.daemon=false -Dorg.gradle.debug=true
```

sudo lsof -t -i tcp:5005 | xargs kill -9
