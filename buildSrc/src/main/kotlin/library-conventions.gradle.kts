plugins {
  id("module-conventions")
  id("org.jetbrains.kotlin.multiplatform")
  id("org.jetbrains.kotlin.plugin.compose")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("org.jetbrains.dokka")
  id("maven-publish")
}

group = "dev.sargunv.maplibre-compose"

kotlin {
  explicitApi()

  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xconsistent-data-class-copy-visibility")
  }
}

composeCompiler { reportsDestination = layout.buildDirectory.dir("compose/reports") }

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
