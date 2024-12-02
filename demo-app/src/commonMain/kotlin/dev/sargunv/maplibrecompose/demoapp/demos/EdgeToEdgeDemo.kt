package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.OrnamentSettings
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import io.github.dellisd.spatialk.geojson.Position

private val PORTLAND = Position(latitude = 45.521, longitude = -122.675)

@Composable
fun EdgeToEdgeDemo(innerPadding: PaddingValues) {
  MaplibreMap(
    styleUrl = DEFAULT_STYLE,
    cameraState = rememberCameraState(CameraPosition(target = PORTLAND, zoom = 13.0)),
    ornamentSettings = OrnamentSettings(padding = innerPadding),
  )
}
