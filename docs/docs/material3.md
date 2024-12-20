# Material 3 extensions

## Controls

We provide reimplementations of certain ornaments using Material 3. These are
regular Compose UI components, so you can position them arbitrarily, style them,
animate them, etc. To use them, disable the default ornaments and add the
Material controls to the map's `content`:

```toml title="libs.versions.toml"
[libraries]
maplibre-composeMaterial3 = { module = "dev.sargunv.maplibre-compose:maplibre-compose-material3", version = "{{ gradle.release_version }}" }
```

```kotlin title="build.gradle.kts"
commonMain.dependencies {
  implementation(libs.maplibre.composeMaterial3)
}
```

```kotlin title="App.kt"
-8<- "demo-app/src/commonMain/kotlin/dev/sargunv/maplibrecompose/demoapp/docs/Material3.kt:controls"
```
