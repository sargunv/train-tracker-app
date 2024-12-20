@file:Suppress("unused")

package dev.sargunv.maplibrecompose.demoapp.docs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.rememberStyleState
import dev.sargunv.maplibrecompose.core.OrnamentSettings
import dev.sargunv.maplibrecompose.material3.controls.AttributionButton
import dev.sargunv.maplibrecompose.material3.controls.CompassButton

@Composable
fun Material3() {
  // -8<- [start:controls]
  val cameraState = rememberCameraState()
  val styleState = rememberStyleState()

  MaplibreMap(
    cameraState = cameraState,
    styleState = styleState,
    ornamentSettings = OrnamentSettings.AllDisabled,
  ) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
      AttributionButton(styleState, modifier = Modifier.align(Alignment.BottomEnd))
      CompassButton(cameraState, modifier = Modifier.align(Alignment.TopEnd))
    }
  }
  // -8<- [end:controls]
}
