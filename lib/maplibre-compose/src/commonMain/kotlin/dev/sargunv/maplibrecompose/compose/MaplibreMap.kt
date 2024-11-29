package dev.sargunv.maplibrecompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrecompose.compose.engine.LayerNode
import dev.sargunv.maplibrecompose.compose.engine.rememberStyleComposition
import dev.sargunv.maplibrecompose.core.MaplibreMap
import dev.sargunv.maplibrecompose.core.Style
import dev.sargunv.maplibrecompose.core.data.GestureSettings
import dev.sargunv.maplibrecompose.core.data.OrnamentSettings
import dev.sargunv.maplibrecompose.core.expression.ExpressionScope
import io.github.dellisd.spatialk.geojson.Position

@Composable
public fun MaplibreMap(
  modifier: Modifier = Modifier,
  styleUrl: String = "https://demotiles.maplibre.org/style.json",
  gestureSettings: GestureSettings = GestureSettings(),
  ornamentSettings: OrnamentSettings = OrnamentSettings(),
  cameraState: CameraState = rememberCameraState(),
  onMapClick: MapClickHandler = { _, _ -> ClickResult.Pass },
  onMapLongClick: MapClickHandler = { _, _ -> ClickResult.Pass },
  isDebugEnabled: Boolean = false,
  maximumFps: Int = 120, // TODO detect device native frame rate
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

        override fun onClick(map: MaplibreMap, latLng: Position, offset: DpOffset) {
          if (onMapClick(latLng, offset).consumed) return
          styleComposition
            ?.children
            ?.asReversed()
            ?.mapNotNull { node -> (node as? LayerNode<*>)?.onClick?.let { node.layer.id to it } }
            ?.find { (layerId, handle) ->
              val features = map.queryRenderedFeatures(offset, setOf(layerId))
              features.isNotEmpty() && handle(features).consumed
            }
        }

        override fun onLongClick(map: MaplibreMap, latLng: Position, offset: DpOffset) {
          if (onMapLongClick(latLng, offset).consumed) return
          styleComposition
            ?.children
            ?.asReversed()
            ?.mapNotNull { node ->
              (node as? LayerNode<*>)?.onLongClick?.let { node.layer.id to it }
            }
            ?.find { (layerId, handle) ->
              val features = map.queryRenderedFeatures(offset, setOf(layerId))
              features.isNotEmpty() && handle(features).consumed
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
      map.setMaximumFps(maximumFps)
    },
    onReset = {
      cameraState.map = null
      rememberedStyle = null
    },
    logger = debugLogger,
    callbacks = callbacks,
  )
}
