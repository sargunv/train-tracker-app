plugins {
  id("org.jetbrains.dokka")
  id("maven-publish")
  id("module-conventions")
}

group = "dev.sargunv.maplibre-compose"

dokka {
  dokkaSourceSets {
    configureEach {
      includes.from("MODULE.md")
      sourceLink {
        remoteUrl("https://github.com/sargunv/maplibre-compose/tree/${project.ext["base_tag"]}/")
        localDirectory.set(rootDir)
      }
      externalDocumentationLinks {
        create("spatial-k") { url("https://dellisd.github.io/spatial-k/api/") }
        create("maplibre-native") {
          url("https://maplibre.org/maplibre-native/android/api/")
          packageListUrl(
            "https://maplibre.org/maplibre-native/android/api/-map-libre%20-native%20-android/package-list"
          )
        }
      }
    }
  }
}

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      setUrl("https://maven.pkg.github.com/sargunv/maplibre-compose")
      credentials {
        username = project.properties["githubUser"]?.toString()
        password = project.properties["githubToken"]?.toString()
      }
    }
  }
}
