dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
  repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
    maven {
      url = uri(File(rootDir, ".m2/repository"))
    }
  }
}

rootProject.name = "AndroidSampleLibrary"
include(":app", ":api", ":core", ":extension", ":plugin")
