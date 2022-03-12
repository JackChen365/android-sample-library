plugins {
  `maven-publish`
  signing
}
val groupName = rootProject.group as String
val versionName = rootProject.version as String

val pomProjectName = "AndroidSampleLibrary"
val pomProjectDesc = "A Gradle Plugin to help automate the generation of android sample app."
val pomProjectUrl = "https://github.com/JackChen365/AndroidSampleLibrary"

val pomInception = "2022"
val pomLicenses = mapOf(
  "The Apache Software License, Version 2.0" to "http://www.apache.org/licenses/LICENSE-2.0.txt"
)

val pomDevelopers = mapOf(
  "JackChen" to "zhenchen@tubi.tv",
  "Airsaid" to "airsaid1024@gmail.com"
)

val pomScmUrl = "https://github.com/JackChen365/AndroidSampleLibrary"
val pomScmConnection = "scm:git:git://github.com/JackChen365/AndroidSampleLibrary.git"
val pomScmDevConnection = "scm:git:ssh://git@github.com/JackChen365/AndroidSampleLibrary.git"

val isAndroidLibrary = project.plugins.hasPlugin("com.android.library")

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("${pomProjectName.capitalize()}Artifact") {
        if (isAndroidLibrary) {
          from(components["release"])
        } else {
          from(components["java"])
        }

        pom {
          // Description
          name.set(pomProjectName)
          description.set(pomProjectDesc)
          url.set(pomProjectUrl)

          // Archive
          groupId = groupName
          version = versionName
          artifactId = "sample-${project.name}"

          // License
          inceptionYear.set(pomInception)
          licenses {
            pomLicenses.forEach { (licenseName, licenseUrl) ->
              license {
                name.set(licenseName)
                url.set(licenseUrl)
              }
            }
          }

          // Developer
          developers {
            pomDevelopers.forEach { (developerName, developerEmail) ->
              developer {
                id.set(developerName.decapitalize())
                name.set(developerName)
                email.set(developerEmail)
              }
            }
          }

          scm {
            url.set(pomScmUrl)
            connection.set(pomScmConnection)
            developerConnection.set(pomScmDevConnection)
          }
        }
      }
    }

    // Configure MavenCentral repository
    // `io.github.gradle-nexus.publish-plugin` plugin is responsible for configuration

  }
}

signing {
  sign(publishing.publications)
}