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
import dev.sargunv.maplibrecompose.core.layer.LineLayer
import dev.sargunv.maplibrecompose.core.source.Source

/**
 * A line layer draws polylines and polygons from the [sourceLayer] in the given [source] in the
 * given style as a series of lines and outlines, respectively. If nothing else is specified, these
 * will be black lines of 1 dp width.
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
 * @param opacity Lines opacity. A value in range `[0..1]`.
 * @param color Lines color.
 *
 *   Ignored if [pattern] is specified.
 *
 * @param dasharray Specifies the lengths of the alternating dashes and gaps that form the dash
 *   pattern. The lengths are later scaled by the line width. To convert a dash length to pixels,
 *   multiply the length by the current line width. Note that GeoJSON sources with `lineMetrics =
 *   true` specified won't render dashed lines to the expected scale. Also note that zoom-dependent
 *   expressions will be evaluated only at integer zoom levels. Ignored if [pattern] is specified.
 * @param pattern Image to use for drawing image lines. For seamless patterns, image width must be a
 *   factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only
 *   at integer zoom levels.
 * @param gradient Defines a gradient with which to color a line feature. Can only be used with
 *   GeoJSON sources that specify `lineMetrics = true`.
 *
 *   Ignored if [pattern] or [dasharray] is specified.
 *
 * @param blur Blur applied to the lines, in dp. A value in range `0..infinity`.
 * @param width Thickness of the lines' stroke in dp. A value in range `[0..infinity)`.
 * @param gapWidth A value in range `[0..infinity)`. If not `0`, instead of one, two lines, each
 *   left and right of each line's actual path are drawn, with the given gap in dp in-between them.
 * @param offset The lines' offset. For linear features, a positive value offsets the line to the
 *   right, relative to the direction of the line, and a negative value to the left. For polygon
 *   features, a positive value results in an inset, and a negative value results in an outset.
 * @param cap Display of line endings. See [StrokeCap].
 * @param join Display of joined lines. See [StrokeJoin].
 * @param miterLimit Limit at which to automatically convert to bevel join for sharp angles when
 *   [join] is [StrokeJoin.Miter].
 * @param roundLimit Limit at which to automatically convert to miter join for sharp angles when
 *   [join] is [StrokeJoin.Round].
 * @param onClick Function to call when any feature in this layer has been clicked.
 * @param onLongClick Function to call when any feature in this layer has been long-clicked.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun LineLayer(
  id: String,
  source: Source,
  sourceLayer: String = "",
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  filter: Expression<Boolean> = nil(),
  visible: Boolean = true,
  sortKey: Expression<Number> = nil(),
  translate: Expression<Point> = point(0, 0),
  translateAnchor: Expression<String> = const(TranslateAnchor.Map),
  opacity: Expression<Number> = const(1),
  color: Expression<Color> = const(Color.Black),
  dasharray: Expression<List<Number>> = nil(),
  pattern: Expression<TResolvedImage> = nil(),
  gradient: Expression<Color> = nil(),
  blur: Expression<Number> = const(0),
  width: Expression<Number> = const(1),
  gapWidth: Expression<Number> = const(0),
  offset: Expression<Number> = const(0),
  cap: Expression<String> = const(StrokeCap.Butt),
  join: Expression<String> = const(StrokeJoin.Miter),
  miterLimit: Expression<Number> = const(2),
  roundLimit: Expression<Number> = const(1.05),
  noinline onClick: FeaturesClickHandler? = null,
  noinline onLongClick: FeaturesClickHandler? = null,
) {
  composeKey(id) {
    LayerNode(
      factory = { LineLayer(id = id, source = source) },
      update = {
        set(sourceLayer) { layer.sourceLayer = it }
        set(minZoom) { layer.minZoom = it }
        set(maxZoom) { layer.maxZoom = it }
        set(filter) { layer.setFilter(it) }
        set(visible) { layer.visible = it }
        set(cap) { layer.setLineCap(it) }
        set(join) { layer.setLineJoin(it) }
        set(miterLimit) { layer.setLineMiterLimit(it) }
        set(roundLimit) { layer.setLineRoundLimit(it) }
        set(sortKey) { layer.setLineSortKey(it) }
        set(opacity) { layer.setLineOpacity(it) }
        set(color) { layer.setLineColor(it) }
        set(translate) { layer.setLineTranslate(it) }
        set(translateAnchor) { layer.setLineTranslateAnchor(it) }
        set(width) { layer.setLineWidth(it) }
        set(gapWidth) { layer.setLineGapWidth(it) }
        set(offset) { layer.setLineOffset(it) }
        set(blur) { layer.setLineBlur(it) }
        set(dasharray) { layer.setLineDasharray(it) }
        set(pattern) { layer.setLinePattern(it) }
        set(gradient) { layer.setLineGradient(it) }
      },
      onClick = onClick,
      onLongClick = onLongClick,
    )
  }
}

/** Display of joined lines */
public object StrokeJoin {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  public const val Bevel: String = "bevel"

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  public const val Round: String = "round"

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  public const val Miter: String = "miter"
}

/** Display of line endings */
public object StrokeCap {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  public const val Butt: String = "butt"

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  public const val Round: String = "round"

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  public const val Square: String = "square"
}
