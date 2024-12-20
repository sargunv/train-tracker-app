import ru.vyarus.gradle.plugin.mkdocs.task.MkdocsTask

plugins {
  id(libs.plugins.spotless.get().pluginId)
  id(libs.plugins.dokka.get().pluginId)
  id(libs.plugins.mkdocs.get().pluginId)
  id("module-conventions")
}

mkdocs {
  sourcesDir = "docs"
  strict = true
  publish {
    docPath = null // single version site
  }
}

tasks.withType<MkdocsTask>().configureEach {
  val releaseVersion = ext["base_tag"].toString().replace("v", "")
  val snapshotVersion = "${ext["next_patch_version"]}-SNAPSHOT"
  extras.set(
    mapOf(
      "release_version" to releaseVersion,
      "snapshot_version" to snapshotVersion,
      "maplibre_ios_version" to libs.versions.maplibre.ios.get(),
      "maplibre_android_version" to libs.versions.maplibre.android.sdk.get(),
    )
  )
}

dokka { moduleName = "MapLibre Compose API Reference" }

tasks.register("generateDocs") {
  dependsOn("dokkaGenerate", "mkdocsBuild")
  doLast {
    copy {
      from(layout.buildDirectory.dir("mkdocs"))
      into(layout.buildDirectory.dir("docs"))
    }
    copy {
      from(layout.buildDirectory.dir("dokka/html"))
      into(layout.buildDirectory.dir("docs/api"))
    }
  }
}

dependencies {
  dokka(project(":lib:maplibre-compose:"))
  dokka(project(":lib:maplibre-compose-material3:"))
}

spotless {
  kotlinGradle {
    target("**/*.gradle.kts")
    ktfmt().googleStyle()
  }
  kotlin {
    target("**/*.kt")
    ktfmt().googleStyle()
  }
  format("swift") {
    target("iosApp/iosApp/**/*.swift")
    nativeCmd("swiftFormat", "/usr/bin/env", listOf("swift", "format"))
  }
  format("markdown") {
    target("**/*.md")
    prettier().config(mapOf("proseWrap" to "always"))
  }
  yaml {
    target("**/*.yml", "**/*.yaml")
    prettier()
  }
}

tasks.register("installGitHooks") {
  doLast {
    copy {
      from("${rootProject.projectDir}/scripts/pre-commit")
      into("${rootProject.projectDir}/.git/hooks")
    }
  }
}

tasks.named("clean") { doLast { delete("${rootProject.projectDir}/.git/hooks/pre-commit") } }
