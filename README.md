[![Maven Central Version](https://img.shields.io/maven-central/v/dev.sargunv.maplibre-compose/maplibre-compose)](https://central.sonatype.com/namespace/dev.sargunv.maplibre-compose)
[![Github Actions](https://github.com/sargunv/maplibre-compose/actions/workflows/ci.yml/badge.svg)](https://github.com/sargunv/maplibre-compose/actions/workflows/ci.yml?query=branch%3Amain)
[![License](https://img.shields.io/github/license/sargunv/maplibre-compose)](https://github.com/sargunv/maplibre-compose/blob/main/LICENSE)

# MapLibre for Compose

This project is a
[Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
library for [MapLibre](https://maplibre.org/). You can use it to embed an
interactive vector map in your Compose app.

Android and iOS support is implemented with
[MapLibre Native](https://github.com/maplibre/maplibre-native).

Desktop and Web are not yet supported.

<p float="left">
  <img src="https://github.com/user-attachments/assets/671fc56d-b1a8-4843-82ea-4813c9712ea3" width="200"/>
  <img src="https://github.com/user-attachments/assets/ae4a43e4-66bc-4a69-824f-957874912abd" width="200"/>
</p>

## Usage

This library is published via
[Maven Central](https://central.sonatype.com/namespace/dev.sargunv.maplibre-compose),
and snapshot builds of `main` are additionally available on
[GitHub Packages](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry).

In your Gradle version catalog, add:

```toml
[libraries]
# ...
maplibre-compose = { module = "dev.sargunv.maplibre-compose:maplibre-compose", version = "0.1.0" }
```

In your Gradle build script, add:

```kotlin
commonMain.dependencies {
  // ...
  implementation(libs.maplibre.compose)
}
```

For iOS, you'll additionally need to add the MapLibre framework to your build.
The easiest way to do this in Kotlin Multiplatform is with the
[CocoaPods Gradle plugin](https://kotlinlang.org/docs/native-cocoapods.html):

```kotlin
cocoapods {
  // ...
  pod("MapLibre", "6.8.1")
}
```

In your app's UI, add a map:

```kotlin
@Composable
fun MyMapScreen() {
  MaplibreMap()
}
```

For full usage information, see the [demo app](./demo-app) and
[docs](https://sargunv.github.io/maplibre-compose/).

## Status

Many common use cases are already supported, but the full breadth of the
MapLibre SDKs is not yet covered. What is already supported may have bugs. API
stability is not yet guaranteed; as we're still exploring how best to express an
interactive map API in Compose.

The following is currently supported:

- Instantiate an interactive map in Compose UI
- Load a [map style](https://maplibre.org/maplibre-style-spec/) via URL,
  including
  - Remote (online) styles
  - Local asset styles with
    [Compose resources](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html#access-the-available-resources-in-your-code)
- Configure the map UI, including:
  - Position ornaments (compass, logo, attribution) with Compose `PaddingValues`
    and alignment
  - Toggle map gestures
- Position or animate the camera and read its state
- Dynamically reload the style (say, to switch themes)
- Add custom layers to the map with a declarative Composable API, including:
  - Injecting layers in between base style layers (say, to render underneath
    labels)
  - Defining GeoJSON sources via URL or
    [Spatial K](https://dellisd.github.io/spatial-k/geojson/) geometry
  - Defining raster and vector sources via URL
  - Defining background, circle, fill, extrusion, heatmap, hillshade, line,
    raster, and symbol
    [layers](https://maplibre.org/maplibre-style-spec/layers/)
  - Configuring layers with
    [expressions](https://maplibre.org/maplibre-style-spec/expressions/)
  - `onClick` and `onLongClick` callbacks with data on which map feature was
    tapped
- Replace base style layers, with all the same functionality above

The following is not yet supported, but planned:

- Add annotations
- Show user location
- Bound the camera
- Show a scale bar
- Configure transitions for transitionable properties
- Convert between screen space and world space coordinates
- Query the map for visible features
- Dynamically configure style properties beyond just sources/layers
- Configure the offline cache
- Interact with base style layers
- Snapshot maps

The following may be explored in the future:

- Support desktop and/or web
- Support for secondary platforms (car?, watch?, tv?)
- Expose map style as mutable state for advanced use cases
- Improve type safety for expressions
- Depend on the MapLibre Native core directly instead of the Android/IOS SDKs
- Re-implement camera gestures in Compose and fully hoist camera state
- Decouple composable style management from map rendering, for use in style
  generation scripts

Contributions are welcome for any of the above!
