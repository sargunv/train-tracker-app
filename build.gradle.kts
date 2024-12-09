import fr.brouillard.oss.jgitver.Strategies
import ru.vyarus.gradle.plugin.mkdocs.task.MkdocsTask

plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.compose) apply false
  alias(libs.plugins.kotlin.composeCompiler) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.mkdocs)
  alias(libs.plugins.jgitver)
}

jgitver {
  strategy(Strategies.MAVEN)
  nonQualifierBranches("main")
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

dependencies { dokka(project(":lib:maplibre-compose:")) }

spotless {
  kotlinGradle { ktfmt().googleStyle() }
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
