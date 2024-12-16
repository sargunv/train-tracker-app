package dev.sargunv.maplibrecompose.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.sargunv.maplibrecompose.core.CameraMoveReason
import dev.sargunv.maplibrecompose.core.GestureSettings
import dev.sargunv.maplibrecompose.core.MaplibreMap
import dev.sargunv.maplibrecompose.core.OrnamentSettings
import dev.sargunv.maplibrecompose.core.Style
import dev.sargunv.maplibrecompose.core.expression.ExpressionScope
import dev.sargunv.maplibrecompose.core.util.PlatformUtils
import io.github.dellisd.spatialk.geojson.Position
import kotlin.math.roundToInt

/**
 * Displays a MapLibre based map.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param styleUri The URI of the map style specification JSON to use, see
 *   [MapLibre Style](https://maplibre.org/maplibre-style-spec/).
 * @param gestureSettings Defines which user map gestures are enabled.
 * @param ornamentSettings Defines which additional UI elements are displayed on top of the map.
 * @param cameraState The camera state specifies what position of the map is rendered, at what zoom,
 *   at what tilt, etc.
 * @param onMapClick Invoked when the map is clicked. A click callback can be defined per layer,
 *   too, see e.g. the `onClick` parameter for
 *   [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer]. However, this callback is
 *   always called first and can thus prevent subsequent callbacks to be invoked by consuming the
 *   event.
 * @param onMapLongClick Invoked when the map is long-clicked. See [onMapClick].
 * @param onFpsChanged Invoked when the frames per second changed
 * @param isDebugEnabled Whether the map debug information is shown.
 * @param maximumFps The maximum frame rate at which the map view is rendered, but it can't excess
 *   the ability of device hardware.
 * @param logger kermit logger to use
 * @param overlay The composable content shown on top of the map.
 * @param content The map content additional to what is already part of the map as defined in the
 *   base map style linked in [styleUri].
 *
 *   Additional [sources](https://maplibre.org/maplibre-style-spec/sources/) can be added via
 *     - [rememberGeoJsonSource][dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource] (
 *       see [GeoJsonSource][dev.sargunv.maplibrecompose.core.source.GeoJsonSource]),
 *     - [rememberVectorSource][dev.sargunv.maplibrecompose.compose.source.rememberVectorSource] (
 *       see [VectorSource][dev.sargunv.maplibrecompose.core.source.VectorSource]),
 *     - [rememberRasterSource][dev.sargunv.maplibrecompose.compose.source.rememberRasterSource] (
 *       see [RasterSource][dev.sargunv.maplibrecompose.core.source.RasterSource])
 *
 *   A source that is already defined in the base map style can be referenced via
 *   [getBaseSource][dev.sargunv.maplibrecompose.compose.source.getBaseSource].
 *
 *   The data from a source can then be used in
 *   [layer](https://maplibre.org/maplibre-style-spec/layers/) definition(s), which define how that
 *   data is rendered, see
 *     - [BackgroundLayer][dev.sargunv.maplibrecompose.compose.layer.BackgroundLayer]
 *     - [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer]
 *     - [FillExtrusionLayer][dev.sargunv.maplibrecompose.compose.layer.FillExtrusionLayer]
 *     - [FillLayer][dev.sargunv.maplibrecompose.compose.layer.FillLayer]
 *     - [HeatmapLayer][dev.sargunv.maplibrecompose.compose.layer.HeatmapLayer]
 *     - [HillshadeLayer][dev.sargunv.maplibrecompose.compose.layer.HillshadeLayer]
 *     - [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer]
 *     - [RasterLayer][dev.sargunv.maplibrecompose.compose.layer.RasterLayer]
 *     - [SymbolLayer][dev.sargunv.maplibrecompose.compose.layer.SymbolLayer]
 *
 *   By default, the layers defined in this scope are put on top of the layers from the base style,
 *   in the order they are defined. Alternatively, it is possible to anchor layers at certain layers
 *   from the base style. This is done, for example, in order to add a layer just below the first
 *   symbol layer from the base style so that it isn't above labels. See
 *     - [Anchor.Top][dev.sargunv.maplibrecompose.compose.layer.Anchor.Companion.Top],
 *     - [Anchor.Bottom][dev.sargunv.maplibrecompose.compose.layer.Anchor.Companion.Bottom],
 *     - [Anchor.Above][dev.sargunv.maplibrecompose.compose.layer.Anchor.Companion.Above],
 *     - [Anchor.Below][dev.sargunv.maplibrecompose.compose.layer.Anchor.Companion.Below],
 *     - [Anchor.Replace][dev.sargunv.maplibrecompose.compose.layer.Anchor.Companion.Replace],
 *     - [Anchor.At][dev.sargunv.maplibrecompose.compose.layer.Anchor.Companion.At]
 */
@Composable
public fun MaplibreMap(
  modifier: Modifier = Modifier,
  styleUri: String = "https://demotiles.maplibre.org/style.json",
  gestureSettings: GestureSettings = GestureSettings.AllEnabled,
  ornamentSettings: OrnamentSettings = OrnamentSettings.AllEnabled,
  cameraState: CameraState = rememberCameraState(),
  onMapClick: MapClickHandler = { _, _ -> ClickResult.Pass },
  onMapLongClick: MapClickHandler = { _, _ -> ClickResult.Pass },
  onFpsChanged: (Double) -> Unit = {},
  isDebugEnabled: Boolean = false,
  maximumFps: Int = PlatformUtils.getSystemRefreshRate().roundToInt(),
  logger: Logger? = remember { Logger.withTag("maplibre-compose") },
  overlay: @Composable BoxScope.() -> Unit = {},
  content: @Composable ExpressionScope.() -> Unit = {},
) {
  var rememberedStyle by remember { mutableStateOf<Style?>(null) }
  val styleComposition by rememberStyleComposition(rememberedStyle, logger, content)

  val callbacks =
    remember(cameraState, styleComposition) {
      object : MaplibreMap.Callbacks {
        override fun onStyleChanged(map: MaplibreMap, style: Style?) {
          rememberedStyle = style
        }

        override fun onCameraMoveStarted(map: MaplibreMap, reason: CameraMoveReason) {
          cameraState.moveReasonState.value = reason
        }

        override fun onCameraMoved(map: MaplibreMap) {
          cameraState.positionState.value = map.cameraPosition
        }

        override fun onCameraMoveEnded(map: MaplibreMap) {}

        private fun layerNodesInOrder(): List<LayerNode<*>> {
          val layerNodes =
            (styleComposition?.children?.filterIsInstance<LayerNode<*>>() ?: emptyList())
              .associateBy { node -> node.layer.id }
          val layers = styleComposition?.styleManager?.style?.getLayers() ?: emptyList()
          return layers.asReversed().mapNotNull { layer -> layerNodes[layer.id] }
        }

        override fun onClick(map: MaplibreMap, latLng: Position, offset: DpOffset) {
          if (onMapClick(latLng, offset).consumed) return
          layerNodesInOrder().find { node ->
            val handle = node.onClick ?: return@find false
            val features =
              map.queryRenderedFeatures(
                offset = offset,
                layerIds = setOf(node.layer.id),
                predicate = null,
              )
            features.isNotEmpty() && handle(features).consumed
          }
        }

        override fun onLongClick(map: MaplibreMap, latLng: Position, offset: DpOffset) {
          if (onMapLongClick(latLng, offset).consumed) return
          layerNodesInOrder().find { node ->
            val handle = node.onLongClick ?: return@find false
            val features =
              map.queryRenderedFeatures(
                offset = offset,
                layerIds = setOf(node.layer.id),
                predicate = null,
              )
            features.isNotEmpty() && handle(features).consumed
          }
        }
      }
    }

  Box(modifier = modifier) {
    ComposableMapView(
      modifier = Modifier.fillMaxSize(),
      styleUri = styleUri,
      update = { map ->
        cameraState.map = map
        map.onFpsChanged = onFpsChanged
        map.isDebugEnabled = isDebugEnabled
        map.setGestureSettings(gestureSettings)
        map.setOrnamentSettings(ornamentSettings)
        map.setMaximumFps(maximumFps)
      },
      onReset = {
        cameraState.map = null
        rememberedStyle = null
      },
      logger = logger,
      callbacks = callbacks,
    )
    overlay()
  }
}
