package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.FeaturesClickHandler
import dev.sargunv.maplibrecompose.core.expression.CirclePitchAlignment
import dev.sargunv.maplibrecompose.core.expression.CirclePitchScale
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.const
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.nil
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.layer.CircleLayer
import dev.sargunv.maplibrecompose.core.source.Source

/**
 * A circle layer draws points from the [sourceLayer] in the given [source] in the given style as a
 * circles. If nothing else is specified, these will be black dots of 5 dp radius.
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
 * @param translateAnchor Frame of reference for offsetting geometry.
 *
 *   Ignored if [translate] is not set.
 *
 * @param opacity Circles opacity. A value in range `[0..1]`.
 * @param color Circles fill color.
 * @param blur Amount to blur the circle. A value of `1` blurs the circle such that only the
 *   centerpoint has full opacity.
 * @param radius Circles radius.
 * @param strokeOpacity Opacity of the circles' stroke.
 * @param strokeColor Circles' stroke color.
 * @param strokeWidth Thickness of the circles' stroke. Strokes are placed outside of the [radius].
 * @param pitchScale Scaling behavior of circles when the map is pitched.
 * @param pitchAlignment Orientation of circles when the map is pitched.
 * @param onClick Function to call when any feature in this layer has been clicked.
 * @param onLongClick Function to call when any feature in this layer has been long-clicked.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun CircleLayer(
  id: String,
  source: Source,
  sourceLayer: String = "",
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  filter: Expression<Boolean> = nil(),
  visible: Boolean = true,
  sortKey: Expression<Number> = nil(),
  translate: Expression<DpOffset> = const(DpOffset.Zero),
  translateAnchor: Expression<TranslateAnchor> = const(TranslateAnchor.Map),
  opacity: Expression<Number> = const(1f),
  color: Expression<Color> = const(Color.Black),
  blur: Expression<Number> = const(0f),
  radius: Expression<Dp> = const(5.dp),
  strokeOpacity: Expression<Number> = const(1f),
  strokeColor: Expression<Color> = const(Color.Black),
  strokeWidth: Expression<Dp> = const(0.dp),
  pitchScale: Expression<CirclePitchScale> = const(CirclePitchScale.Map),
  pitchAlignment: Expression<CirclePitchAlignment> = const(CirclePitchAlignment.Viewport),
  noinline onClick: FeaturesClickHandler? = null,
  noinline onLongClick: FeaturesClickHandler? = null,
) {
  key(id) {
    LayerNode(
      factory = { CircleLayer(id = id, source = source) },
      update = {
        set(sourceLayer) { layer.sourceLayer = it }
        set(minZoom) { layer.minZoom = it }
        set(maxZoom) { layer.maxZoom = it }
        set(filter) { layer.setFilter(it) }
        set(visible) { layer.visible = it }
        set(sortKey) { layer.setCircleSortKey(it) }
        set(radius) { layer.setCircleRadius(it) }
        set(color) { layer.setCircleColor(it) }
        set(blur) { layer.setCircleBlur(it) }
        set(opacity) { layer.setCircleOpacity(it) }
        set(translate) { layer.setCircleTranslate(it) }
        set(translateAnchor) { layer.setCircleTranslateAnchor(it) }
        set(pitchScale) { layer.setCirclePitchScale(it) }
        set(pitchAlignment) { layer.setCirclePitchAlignment(it) }
        set(strokeWidth) { layer.setCircleStrokeWidth(it) }
        set(strokeColor) { layer.setCircleStrokeColor(it) }
        set(strokeOpacity) { layer.setCircleStrokeOpacity(it) }
      },
      onClick = onClick,
      onLongClick = onLongClick,
    )
  }
}
