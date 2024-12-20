@file:Suppress("unused", "UNUSED_ANONYMOUS_PARAMETER")

package dev.sargunv.maplibrecompose.demoapp.docs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.ClickResult
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.rememberStyleState
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.GestureSettings
import dev.sargunv.maplibrecompose.core.OrnamentSettings
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.material3.controls.AttributionButton
import dev.sargunv.maplibrecompose.material3.controls.CompassButton
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds

@Composable
fun Interaction() {
  // -8<- [start:gesture-settings]
  MaplibreMap(
    gestureSettings =
      GestureSettings(
        isTiltGesturesEnabled = true,
        isZoomGesturesEnabled = true, // (1)!
        isRotateGesturesEnabled = true,
        isScrollGesturesEnabled = true,
      )
  )
  // -8<- [end:gesture-settings]

  // -8<- [start:ornament-settings]
  MaplibreMap(
    ornamentSettings =
      OrnamentSettings(
        padding = PaddingValues(0.dp), // (1)!
        isLogoEnabled = true, // (2)!
        logoAlignment = Alignment.BottomStart, // (3)!
        isAttributionEnabled = true, // (4)!
        attributionAlignment = Alignment.BottomEnd,
        isCompassEnabled = true, // (5)!
        compassAlignment = Alignment.TopEnd,
        isScaleBarEnabled = true, // (6)!
        scaleBarAlignment = Alignment.TopStart,
      )
  )
  // -8<- [end:ornament-settings]

  // -8<- [start:camera]
  val camera =
    rememberCameraState(
      firstPosition =
        CameraPosition(target = Position(latitude = 45.521, longitude = -122.675), zoom = 13.0)
    )
  MaplibreMap(cameraState = camera)
  // -8<- [end:camera]

  // -8<- [start:camera-animate]
  LaunchedEffect(Unit) {
    camera.animateTo(
      finalPosition =
        camera.position.copy(target = Position(latitude = 47.607, longitude = -122.342)),
      duration = 3.seconds,
    )
  }
  // -8<- [end:camera-animate]

  // -8<- [start:click-listeners]
  MaplibreMap(
    cameraState = camera,
    onMapClick = { pos, offset ->
      val features = camera.queryRenderedFeatures(offset)
      if (features.isNotEmpty()) {
        println("Clicked on ${features[0].json()}")
        ClickResult.Consume // (1)!
      } else {
        ClickResult.Pass
      }
    },
    onMapLongClick = { pos, offset ->
      println("Long click at $pos")
      ClickResult.Pass
    },
  )
  // -8<- [end:click-listeners]
}

@Composable
fun Material3() {
  // -8<- [start:material3]
  val cameraState = rememberCameraState()
  val styleState = rememberStyleState()

  MaplibreMap(
    styleUri = DEFAULT_STYLE,
    cameraState = cameraState,
    styleState = styleState,
    ornamentSettings = OrnamentSettings.AllDisabled,
  ) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
      AttributionButton(styleState, modifier = Modifier.align(Alignment.BottomEnd))
      CompassButton(cameraState, modifier = Modifier.align(Alignment.TopEnd))
    }
  }
  // -8<- [end:material3]
}

// @Composable
// override fun Component(navigateUp: () -> Unit) {
//  val cameraState = rememberCameraState(CameraPosition(target = PORTLAND, zoom = 13.0))
//  val styleState = rememberStyleState()
//
//  Scaffold(
//    topBar = { DemoAppBar(this, navigateUp, alpha = 0.5f) },
//    content = { padding ->
//      MaplibreMap(
//        modifier = Modifier.consumeWindowInsets(padding),
//        styleUri = DEFAULT_STYLE,
//        cameraState = cameraState,
//        styleState = styleState,
//        ornamentSettings =
//        OrnamentSettings(
//          padding = padding,
//          isCompassEnabled = false,
//          isLogoEnabled = false,
//          isAttributionEnabled = false,
//        ),
//        content = {
//          Box(modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp)) {
//            AttributionButton(styleState, modifier = Modifier.align(Alignment.BottomEnd))
//            CompassButton(cameraState, modifier = Modifier.align(Alignment.TopEnd))
//          }
//        },
//      )
//    },
//  )
// }
