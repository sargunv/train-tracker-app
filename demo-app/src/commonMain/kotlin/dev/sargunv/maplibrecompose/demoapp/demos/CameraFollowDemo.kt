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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.CameraState
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.layer.CircleLayer
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.const
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.FrameRateState
import dev.sargunv.maplibrecompose.demoapp.PositionVectorConverter
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Position

private val START_POINT = Position(longitude = -122.4194, latitude = 37.7749)
private val END_POINT = Position(longitude = -122.3954, latitude = 37.7939)
private const val MIN_ZOOM = 9
private const val MAX_ZOOM = 15

@Composable
fun CameraFollowDemo() = Column {
  val animatedPosition by animateTestPosition(START_POINT, END_POINT)
  var zoom by remember { mutableStateOf((MIN_ZOOM + MAX_ZOOM) / 2) }
  val camera = rememberAnimatedFollowCamera(target = animatedPosition, zoom = zoom.toFloat())
  val fpsState = remember { FrameRateState() }

  MaplibreMap(
    modifier = Modifier.weight(1f),
    styleUrl = DEFAULT_STYLE,
    cameraState = camera,
    onFpsChanged = fpsState::recordFps,
  ) {
    LocationPuck(locationSource = rememberGeoJsonSource("target", Point(animatedPosition)))
  }

  FollowControls(
    currentZoom = zoom,
    onZoomChange = { zoom = it },
    text = "FPS: ${fpsState.spinChar} ${fpsState.avgFps}",
  )
}

@Composable
private fun animateTestPosition(start: Position, end: Position): State<Position> {
  val infiniteTransition = rememberInfiniteTransition()
  return infiniteTransition.animateValue(
    start,
    end,
    typeConverter = remember { PositionVectorConverter(origin = START_POINT) },
    animationSpec =
      remember {
        infiniteRepeatable(
          animation = tween(durationMillis = 30_000),
          repeatMode = RepeatMode.Reverse,
        )
      },
  )
}

@Composable
private fun rememberAnimatedFollowCamera(target: Position, zoom: Float): CameraState {
  val animatedZoom by animateFloatAsState(zoom.toFloat())

  val cameraState =
    rememberCameraState(
      firstPosition = CameraPosition(target = target, zoom = animatedZoom.toDouble())
    )

  // TODO stop enforcing the camera state when the user pans
  SideEffect {
    cameraState.position =
      cameraState.position.copy(target = target, zoom = animatedZoom.toDouble())
  }

  return cameraState
}

@Composable
private fun LocationPuck(locationSource: Source) {
  CircleLayer(
    id = "target-shadow",
    source = locationSource,
    radius = const(13.dp),
    color = const(Color.Black),
    blur = const(1f),
    translate = const(DpOffset(0.dp, 1.dp)),
  )

  CircleLayer(
    id = "target-circle",
    source = locationSource,
    radius = const(7.dp),
    color = const(MaterialTheme.colorScheme.primary),
    strokeColor = const(Color.White),
    strokeWidth = const(3.dp),
  )
}

@Composable
private fun FollowControls(currentZoom: Int, onZoomChange: (Int) -> Unit, text: String) {
  Row(
    modifier = Modifier.padding(16.dp).fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    Button(
      enabled = currentZoom > MIN_ZOOM,
      onClick = { onZoomChange((currentZoom - 1).coerceAtLeast(MIN_ZOOM)) },
    ) {
      Text("Zoom out")
    }
    Text(text = text)
    Button(
      enabled = currentZoom < MAX_ZOOM,
      onClick = { onZoomChange((currentZoom + 1).coerceAtMost(MAX_ZOOM)) },
    ) {
      Text("Zoom in")
    }
  }
}
