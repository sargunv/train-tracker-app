package dev.sargunv.maplibrekmp.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrekmp.compose.engine.LayerNode
import dev.sargunv.maplibrekmp.compose.engine.rememberStyleComposition
import dev.sargunv.maplibrekmp.core.MaplibreMap
import dev.sargunv.maplibrekmp.core.Style
import dev.sargunv.maplibrekmp.core.data.GestureSettings
import dev.sargunv.maplibrekmp.core.data.OrnamentSettings
import dev.sargunv.maplibrekmp.core.data.XY
import dev.sargunv.maplibrekmp.expression.ExpressionScope
import io.github.dellisd.spatialk.geojson.Position

@Composable
public fun MaplibreMap(
  modifier: Modifier = Modifier,
  styleUrl: String = "https://demotiles.maplibre.org/style.json",
  gestureSettings: GestureSettings = GestureSettings(),
  ornamentSettings: OrnamentSettings = OrnamentSettings(),
  cameraState: CameraState = rememberCameraState(),
  isDebugEnabled: Boolean = false,
  debugLogger: Logger? = remember { Logger.withTag("maplibre-compose") },
  content: @Composable ExpressionScope.() -> Unit = {},
) {
  var rememberedStyle by remember { mutableStateOf<Style?>(null) }
  val styleComposition by rememberStyleComposition(rememberedStyle, debugLogger, content)

  val callbacks =
    remember(cameraState, styleComposition) {
      object : MaplibreMap.Callbacks {
        override fun onStyleChanged(map: MaplibreMap, style: Style?) {
          rememberedStyle = style
        }

        override fun onCameraMove(map: MaplibreMap) {
          cameraState.positionState.value = map.cameraPosition
        }

        override fun onClick(map: MaplibreMap, latLng: Position, xy: XY) {
          styleComposition
            ?.children
            ?.mapNotNull { node -> (node as? LayerNode<*>)?.onClick?.let { node.layer.id to it } }
            ?.forEach { (layerId, handle) ->
              val features = map.queryRenderedFeatures(xy, setOf(layerId))
              if (features.isNotEmpty()) handle(features)
            }
        }

        override fun onLongClick(map: MaplibreMap, latLng: Position, xy: XY) {
          styleComposition
            ?.children
            ?.mapNotNull { node ->
              (node as? LayerNode<*>)?.onLongClick?.let { node.layer.id to it }
            }
            ?.forEach { (layerId, handle) ->
              val features = map.queryRenderedFeatures(xy, setOf(layerId))
              if (features.isNotEmpty()) handle(features)
            }
        }
      }
    }

  ComposableMapView(
    modifier = modifier,
    styleUrl = styleUrl,
    update = { map ->
      cameraState.map = map
      map.isDebugEnabled = isDebugEnabled
      map.setGestureSettings(gestureSettings)
      map.setOrnamentSettings(ornamentSettings)
    },
    onReset = {
      cameraState.map = null
      rememberedStyle = null
    },
    logger = debugLogger,
    callbacks = callbacks,
  )
}

