package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import dev.sargunv.maplibrecompose.compose.ClickResult
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.layer.SymbolLayer
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.rememberStyleState
import dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.const
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.image
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.Demo
import dev.sargunv.maplibrecompose.demoapp.DemoMapControls
import dev.sargunv.maplibrecompose.demoapp.DemoOrnamentSettings
import dev.sargunv.maplibrecompose.demoapp.DemoScaffold
import dev.sargunv.maplibrecompose.demoapp.generated.Res
import dev.sargunv.maplibrecompose.demoapp.generated.marker
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import org.jetbrains.compose.resources.painterResource

@Composable
private fun Painter.rememberAsBitmap(): ImageBitmap {
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current
  return remember(this, density, layoutDirection) {
    ImageBitmap(intrinsicSize.width.toInt(), intrinsicSize.height.toInt()).also { bitmap ->
      CanvasDrawScope().draw(density, layoutDirection, Canvas(bitmap), intrinsicSize) { draw(size) }
    }
  }
}

private val CHICAGO = Position(latitude = 41.878, longitude = -87.626)

object MarkersDemo : Demo {
  override val name = "Markers"
  override val description = "Add and interact with markers"

  @Composable
  override fun Component(navigateUp: () -> Unit) {
    DemoScaffold(this, navigateUp) {
      val marker = painterResource(Res.drawable.marker).rememberAsBitmap()
      val cameraState =
        rememberCameraState(firstPosition = CameraPosition(target = CHICAGO, zoom = 7.0))
      val styleState = rememberStyleState(images = mapOf("demo-marker" to marker))
      var selectedFeature by remember { mutableStateOf<Feature?>(null) }

      Box(modifier = Modifier.fillMaxSize()) {
        MaplibreMap(
          styleUri = DEFAULT_STYLE,
          cameraState = cameraState,
          styleState = styleState,
          ornamentSettings = DemoOrnamentSettings(),
        ) {
          val amtrakStations =
            rememberGeoJsonSource(
              id = "amtrak-stations",
              uri =
                "https://raw.githubusercontent.com/datanews/amtrak-geojson/refs/heads/master/amtrak-stations.geojson",
            )
          SymbolLayer(
            id = "amtrak-stations",
            source = amtrakStations,
            onClick = { features ->
              selectedFeature = features.firstOrNull()
              ClickResult.Consume
            },
            iconImage = image(const("demo-marker")),
            iconAllowOverlap = const(true),
          )
        }
        DemoMapControls(cameraState, styleState)
      }

      selectedFeature?.let { feature ->
        AlertDialog(
          onDismissRequest = { selectedFeature = null },
          confirmButton = {},
          title = { Text(feature.getStringProperty("STNNAME") ?: "") },
          text = {
            Column {
              Text("Station Code: ${feature.getStringProperty("STNCODE") ?: ""}")
              Text("Station Type: ${feature.getStringProperty("STNTYPE") ?: ""}")
              Text("Address: ${feature.getStringProperty("ADDRESS1") ?: ""}")
              Text("City: ${feature.getStringProperty("CITY") ?: ""}")
              Text("State: ${feature.getStringProperty("STATE") ?: ""}")
              Text("Zip: ${feature.getStringProperty("ZIP") ?: ""}")
            }
          },
        )
      }
    }
  }
}