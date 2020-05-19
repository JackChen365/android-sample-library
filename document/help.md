## Help

* The command of debug processor

```
./gradlew compileDebugJavaWithJavac -Dorg.gradle.daemon=false -Dorg.gradle.debug=true
```

sudo lsof -t -i tcp:5005 | xargs kill -9

* The command of debug plugin task

```
./gradlew :app:transformClassesWithSampleForDebug -Dorg.gradle.daemon=false -Dorg.gradle.debug=true
./gradlew :app:jar -Dorg.gradle.daemon=false -Dorg.gradle.debug=true
```
