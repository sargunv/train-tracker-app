package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.controls.AttributionButton
import dev.sargunv.maplibrecompose.compose.controls.CompassButton
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.rememberStyleState
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.OrnamentSettings
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
    val cameraState = rememberCameraState(CameraPosition(target = PORTLAND, zoom = 13.0))
    val styleState = rememberStyleState()

    Scaffold(
      topBar = { DemoAppBar(this, navigateUp, alpha = 0.5f) },
      content = { padding ->
        MaplibreMap(
          modifier = Modifier.consumeWindowInsets(padding),
          styleUri = DEFAULT_STYLE,
          cameraState = cameraState,
          styleState = styleState,
          ornamentSettings =
            OrnamentSettings(
              padding = padding,
              isCompassEnabled = false,
              isLogoEnabled = false,
              isAttributionEnabled = false,
            ),
          overlay = {
            Box(modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp)) {
              AttributionButton(styleState, modifier = Modifier.align(Alignment.BottomEnd))
              CompassButton(cameraState, modifier = Modifier.align(Alignment.TopEnd))
            }
          },
        )
      },
    )
  }
}
