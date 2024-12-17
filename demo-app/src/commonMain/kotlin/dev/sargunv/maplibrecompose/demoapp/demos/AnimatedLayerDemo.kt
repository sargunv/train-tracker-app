package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.layer.Anchor
import dev.sargunv.maplibrecompose.compose.layer.LineLayer
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.const
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.exponential
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.interpolate
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.zoom
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.Demo
import dev.sargunv.maplibrecompose.demoapp.DemoScaffold
import dev.sargunv.maplibrecompose.demoapp.generated.Res
import io.github.dellisd.spatialk.geojson.Position
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val ROUTES_FILE = "files/data/amtrak_routes.geojson"

private val US = Position(latitude = 46.336, longitude = -96.205)

object AnimatedLayerDemo : Demo {
  override val name = "Animated layer"
  override val description = "Change layer properties at runtime."

  @Composable
  @OptIn(ExperimentalResourceApi::class)
  override fun Component(navigateUp: () -> Unit) {
    DemoScaffold(this, navigateUp) {
      MaplibreMap(
        styleUri = DEFAULT_STYLE,
        cameraState = rememberCameraState(firstPosition = CameraPosition(target = US, zoom = 2.0)),
        mapContent = {
          val routeSource =
            rememberGeoJsonSource(id = "amtrak-routes", uri = Res.getUri(ROUTES_FILE))

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

          Anchor.Below("waterway_line_label") {
            LineLayer(
              id = "amtrak-routes",
              source = routeSource,
              color = const(animatedColor),
              cap = const(LineCap.Round),
              join = const(LineJoin.Round),
              width =
                interpolate(
                  type = exponential(const(1.2f)),
                  input = zoom(),
                  7 to const(1.75.dp),
                  20 to const(22.dp),
                ),
            )
          }
        },
      )
    }
  }
}
