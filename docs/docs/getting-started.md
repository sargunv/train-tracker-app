# Getting Started

## Installation

This library is published via [Maven Central][maven], and snapshot builds of
`main` are additionally available on [GitHub Packages][gh-packages].

=== "Releases (Maven Central)"

    The latest release is **v{{ gradle.base_version }}**. In your Gradle version catalog, add:

    ```toml
    [libraries]
    maplibre-compose = { module = "dev.sargunv.maplibre-compose:maplibre-compose", version = "{{ gradle.base_version }}" }
    ```

=== "Snapshots (GitHub Packages)"

    !!! warning

        Note that the published documentation is for the latest release, and may not match the
        snapshot version. If using snapshots, always refer to the [latest source code][repo] for the
        most accurate information.

    First, follow [GitHub's guide][gh-packages-guide] for authenticating to GitHub Packages. Your
    settings.gradle.kts should have something like this:

    ```kotlin
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

    The latest snapshot is **v{{ gradle.version }}**. In your Gradle version catalog, add:

    ```toml
    [libraries]
    maplibre-compose = { module = "dev.sargunv.maplibre-compose:maplibre-compose", version = "{{ gradle.version }}" }
    ```

In your Gradle build script, add:

```kotlin
commonMain.dependencies {
  implementation(libs.maplibre.compose)
}
```

For iOS, you'll additionally need to add the MapLibre framework to your build.
The easiest way to do this in Kotlin Multiplatform is with the [CocoaPods Gradle
plugin][kotlin-cocoapods]:

```kotlin
cocoapods {
  pod("MapLibre", "{{ gradle.maplibre_ios_version }}")
}
```

## Usage

In your Composable UI, add a map:

```kotlin
@Composable
fun MyScreen() {
  MaplibreMap()
}
```

For full usage information, see the [demo app][repo-demo] and
[API reference](./api/index.html).

[maven]: https://central.sonatype.com/namespace/dev.sargunv.maplibre-compose
[gh-packages]:
  https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry
[gh-packages-guide]:
  https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package
[kotlin-cocoapods]: https://kotlinlang.org/docs/native-cocoapods.html
[repo]: https://github.com/sargunv/maplibre-compose
[repo-demo]: https://github.com/sargunv/maplibre-compose/tree/main/demo-app
