# Getting started

This documentation assumes you already have a Compose Multiplatform project set
up. If you haven't already, follow [the official JetBrains
documentation][compose-guide] to set up a project.

## Add the library to your app

This library is published via [Maven Central][maven], and snapshot builds of
`main` are additionally available on [GitHub Packages][gh-packages].

=== "Releases (Maven Central)"

    The latest release is **v{{ gradle.release_version }}**. In your Gradle version catalog, add:

    ```toml title="libs.versions.toml"
    [libraries]
    maplibre-compose = { module = "dev.sargunv.maplibre-compose:maplibre-compose", version = "{{ gradle.release_version }}" }
    ```

=== "Snapshots (GitHub Packages)"

    !!! warning

        Note that the published documentation is for the latest release, and may not match the
        snapshot version. If using snapshots, always refer to the [latest source code][repo] for the
        most accurate information.

    First, follow [GitHub's guide][gh-packages-guide] for authenticating to GitHub Packages. Your
    settings.gradle.kts should have something like this:

    ```kotlin title="settings.gradle.kts"
    repositories {
      maven {
        url = uri("https://maven.pkg.github.com/sargunv/maplibre-compose")
        credentials {
          username = project.findProperty("gpr.user") as String? ?: System.getenv("GH_USERNAME")
          password = project.findProperty("gpr.key") as String? ?: System.getenv("GH_TOKEN")
        }
      }
    }
    ```

    The latest snapshot is **v{{ gradle.snapshot_version }}**. In your Gradle version catalog, add:

    ```toml title="libs.versions.toml"
    [libraries]
    maplibre-compose = { module = "dev.sargunv.maplibre-compose:maplibre-compose", version = "{{ gradle.snapshot_version }}" }
    ```

In your Gradle build script, add:

```kotlin title="build.gradle.kts"
commonMain.dependencies {
  implementation(libs.maplibre.compose)
}
```

## Set up iOS

For iOS, you'll additionally need to add the MapLibre framework to your build.
The easiest way to do this in Kotlin Multiplatform is with the [CocoaPods Gradle
plugin][kotlin-cocoapods]:

```kotlin title="build.gradle.kts"
cocoapods {
  pod("MapLibre", "{{ gradle.maplibre_ios_version }}")
}
```

## Display your first map

In your Composable UI, add a map:

```kotlin title="App.kt"
-8<- "demo-app/src/commonMain/kotlin/dev/sargunv/maplibrecompose/demoapp/docs/GettingStarted.kt:app"
```

When you run your app, you should see the default [demotiles] map. To learn how
to get a detailed map with all the features you'd expect, proceed to
[Styling](./styling.md).

[compose-guide]:
  https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-create-first-app.html
[maven]: https://central.sonatype.com/namespace/dev.sargunv.maplibre-compose
[gh-packages]:
  https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry
[gh-packages-guide]:
  https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package
[kotlin-cocoapods]: https://kotlinlang.org/docs/native-cocoapods.html
[repo]: https://github.com/sargunv/maplibre-compose
[demotiles]: https://demotiles.maplibre.org/
