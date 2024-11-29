package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.layer.CircleLayer
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource
import dev.sargunv.maplibrecompose.core.camera.CameraPosition
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.PositionVectorConverter
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Position

private val START_POINT = Position(longitude = -122.4194, latitude = 37.7749)
private val END_POINT = Position(longitude = -122.3954, latitude = 37.7939)
private const val MIN_ZOOM = 9
private const val MAX_ZOOM = 15

@Composable
fun CameraFollowDemo() = Column {
  val infiniteTransition = rememberInfiniteTransition()

  val animatedPosition by
    infiniteTransition.animateValue(
      START_POINT,
      END_POINT,
      typeConverter = PositionVectorConverter(origin = START_POINT),
      animationSpec =
        infiniteRepeatable(
          animation = tween(durationMillis = 30_000),
          repeatMode = RepeatMode.Reverse,
        ),
    )

  var desiredZoom by remember { mutableStateOf((MIN_ZOOM + MAX_ZOOM) / 2) }
  val animatedZoom by animateFloatAsState(desiredZoom.toFloat())

  val cameraState =
    rememberCameraState(
      firstPosition = CameraPosition(target = animatedPosition, zoom = animatedZoom.toDouble())
    )

  // TODO stop enforcing the camera state when the user pans
  SideEffect {
    cameraState.position =
      cameraState.position.copy(target = animatedPosition, zoom = animatedZoom.toDouble())
  }

  MaplibreMap(modifier = Modifier.weight(1f), styleUrl = DEFAULT_STYLE, cameraState = cameraState) {
    val targetSource = rememberGeoJsonSource("target", Point(animatedPosition))

    CircleLayer(
      id = "target-shadow",
      source = targetSource,
      radius = const(13),
      color = const(Color.Black),
      blur = const(1),
      translate = point(0, 1),
    )

    CircleLayer(
      id = "target-circle",
      source = targetSource,
      radius = const(7),
      color = const(MaterialTheme.colorScheme.primary),
      strokeColor = const(Color.White),
      strokeWidth = const(3),
    )
  }

  Row(
    modifier = Modifier.padding(16.dp).fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    Button(
      enabled = desiredZoom > MIN_ZOOM,
      onClick = { desiredZoom = (desiredZoom - 1).coerceAtLeast(MIN_ZOOM) },
    ) {
      Text("Zoom out")
    }
    Button(
      enabled = desiredZoom < MAX_ZOOM,
      onClick = { desiredZoom = (desiredZoom + 1).coerceAtMost(MAX_ZOOM) },
    ) {
      Text("Zoom in")
    }
  }
}
