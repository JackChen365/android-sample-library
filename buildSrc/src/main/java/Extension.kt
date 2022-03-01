import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.tencentMirror() {
  maven("http://mirrors.cloud.tencent.com/nexus/repository/maven-public/") {
    isAllowInsecureProtocol = true
  }
}

fun RepositoryHandler.aliMirror() {
  maven("http://maven.aliyun.com/nexus/content/groups/public/") {
    isAllowInsecureProtocol = true
  }
}

fun RepositoryHandler.jitpack() {
  maven("https://jitpack.io")
}