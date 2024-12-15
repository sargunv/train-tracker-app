package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.Demo
import dev.sargunv.maplibrecompose.demoapp.DemoAppBar
import io.github.dellisd.spatialk.geojson.Position

private val PORTLAND = Position(latitude = 45.521, longitude = -122.675)

object EdgeToEdgeDemo : Demo {
  override val name = "Edge-to-edge"
  override val description =
    "Fill the entire screen with a map and pad ornaments to position them correctly."

  @Composable
  override fun Component(navigateUp: () -> Unit) {
    Scaffold(
      topBar = { DemoAppBar(this, navigateUp, alpha = 0.5f) },
      content = { padding ->
        MaplibreMap(
          modifier = Modifier.consumeWindowInsets(padding),
          styleUri = DEFAULT_STYLE,
          cameraState = rememberCameraState(CameraPosition(target = PORTLAND, zoom = 13.0)),
        )
      },
    )
  }
}
