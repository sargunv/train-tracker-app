package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key as composeKey
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.compose.FeaturesClickHandler
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.const
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.nil
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.point
import dev.sargunv.maplibrecompose.core.expression.Point
import dev.sargunv.maplibrecompose.core.expression.TResolvedImage
import dev.sargunv.maplibrecompose.core.layer.FillLayer
import dev.sargunv.maplibrecompose.core.source.Source

/**
 * A fill layer draws polygons from the [sourceLayer] in the given [source] in the given style as a
 * series of polygon fills. If nothing else is specified, these will be black.
 *
 * @param id Unique layer name.
 * @param source Vector data source for this layer.
 * @param sourceLayer Layer to use from the given vector tile [source].
 * @param minZoom The minimum zoom level for the layer. At zoom levels less than this, the layer
 *   will be hidden. A value in the range of `[0..24]`.
 * @param maxZoom The maximum zoom level for the layer. At zoom levels equal to or greater than
 *   this, the layer will be hidden. A value in the range of `[0..24]`.
 * @param filter An expression specifying conditions on source features. Only features that match
 *   the filter are displayed. Zoom expressions in filters are only evaluated at integer zoom
 *   levels. The `feature-state` expression is not supported in filter expressions.
 * @param visible Whether the layer should be displayed.
 * @param sortKey Sorts features within this layer in ascending order based on this value. Features
 *   with a higher sort key will appear above features with a lower sort key.
 * @param translate The geometry's offset relative to the [translateAnchor]. Negative numbers
 *   indicate left and up, respectively.
 * @param translateAnchor Frame of reference for offsetting geometry, see [TranslateAnchor].
 *
 *   Ignored if [translate] is not set.
 *
 * @param opacity Fill opacity. A value in range `[0..1]`.
 * @param color Fill color.
 *
 *   Ignored if [pattern] is specified.
 *
 * @param pattern Image to use for drawing image fills. For seamless patterns, image width and
 *   height must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will
 *   be evaluated only at integer zoom levels.
 * @param antialias Whether or not the fill should be antialiased.
 * @param outlineColor The outline color of the fill. The outline is drawn at a hairline width.
 *
 *   Ignored if [antialias] is `false`.
 *
 * @param onClick Function to call when any feature in this layer has been clicked.
 * @param onLongClick Function to call when any feature in this layer has been long-clicked.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun FillLayer(
  id: String,
  source: Source,
  sourceLayer: String = "",
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  filter: Expression<Boolean> = nil(),
  visible: Boolean = true,
  sortKey: Expression<Number> = nil(),
  translate: Expression<Point> = point(0.0, 0.0),
  translateAnchor: Expression<String> = const(TranslateAnchor.Map),
  opacity: Expression<Number> = const(1.0),
  color: Expression<Color> = const(Color.Black),
  pattern: Expression<TResolvedImage> = nil(),
  antialias: Expression<Boolean> = const(true),
  outlineColor: Expression<Color> = color,
  noinline onClick: FeaturesClickHandler? = null,
  noinline onLongClick: FeaturesClickHandler? = null,
) {
  composeKey(id) {
    LayerNode(
      factory = { FillLayer(id = id, source = source) },
      update = {
        set(sourceLayer) { layer.sourceLayer = it }
        set(minZoom) { layer.minZoom = it }
        set(maxZoom) { layer.maxZoom = it }
        set(filter) { layer.setFilter(it) }
        set(visible) { layer.visible = it }
        set(sortKey) { layer.setFillSortKey(it) }
        set(antialias) { layer.setFillAntialias(it) }
        set(opacity) { layer.setFillOpacity(it) }
        set(color) { layer.setFillColor(it) }
        set(outlineColor) { layer.setFillOutlineColor(it) }
        set(translate) { layer.setFillTranslate(it) }
        set(translateAnchor) { layer.setFillTranslateAnchor(it) }
        set(pattern) { layer.setFillPattern(it) }
      },
      onClick = onClick,
      onLongClick = onLongClick,
    )
  }
}
