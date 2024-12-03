plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.jetbrains.compose) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.mkdocs)
}

mkdocs { sourcesDir = "docs" }

dokka { moduleName = "MapLibre Compose API Reference" }

dependencies { dokka(project(":lib:maplibre-compose:")) }

spotless {
  kotlinGradle { ktfmt().googleStyle() }
  format("swift") {
    target("iosApp/iosApp/**/*.swift")
    nativeCmd("swiftFormat", "/usr/bin/env", listOf("swift", "format"))
  }
  flexmark {
    target("**/*.md")
    flexmark()
  }
  yaml {
    target("**/*.yml", "**/*.yaml")
    prettier().config(mapOf("quoteProps" to "consistent"))
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
