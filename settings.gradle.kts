dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
  repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
  }
}

rootProject.name = "AndroidSampleLibrary"
include(":app", ":api", ":core", ":extension", ":plugin")
