package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.layer.AnchorBelow
import dev.sargunv.maplibrecompose.compose.layer.LineLayer
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource
import dev.sargunv.maplibrecompose.core.camera.CameraPosition
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.generated.Res
import io.github.dellisd.spatialk.geojson.Position
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val ROUTES_FILE = "files/data/amtrak_routes.geojson"

private val US = Position(latitude = 46.336, longitude = -96.205)

@Composable
@OptIn(ExperimentalResourceApi::class)
fun AnimatedLayerDemo() = Column {
  MaplibreMap(
    modifier = Modifier.weight(1f),
    styleUrl = DEFAULT_STYLE,
    cameraState = rememberCameraState(firstPosition = CameraPosition(target = US, zoom = 2.0)),
  ) {
    val routeSource = rememberGeoJsonSource(id = "amtrak-routes", dataUrl = Res.getUri(ROUTES_FILE))

    val infiniteTransition = rememberInfiniteTransition()
    val animatedColor by
      infiniteTransition.animateColor(
        Color.hsl(0f, 1f, 0.5f),
        Color.hsl(0f, 1f, 0.5f),
        animationSpec =
          infiniteRepeatable(
            animation =
              keyframes {
                durationMillis = 10000
                for (i in 1..9) Color.hsl(i * 36f, 1f, 0.5f) at (i * 1000)
              }
          ),
      )

    AnchorBelow("waterway_line_label") {
      LineLayer(
        id = "amtrak-routes",
        source = routeSource,
        color = const(animatedColor),
        width = const(4),
      )
    }
  }
}
