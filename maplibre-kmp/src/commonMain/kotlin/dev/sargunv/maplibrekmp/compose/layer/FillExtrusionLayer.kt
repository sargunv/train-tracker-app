package dev.sargunv.maplibrekmp.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrekmp.core.layer.FillExtrusionLayer
import dev.sargunv.maplibrekmp.core.source.Source
import dev.sargunv.maplibrekmp.expression.Expression
import dev.sargunv.maplibrekmp.expression.Expression.Companion.const
import dev.sargunv.maplibrekmp.expression.Expression.Companion.nil
import dev.sargunv.maplibrekmp.expression.Expression.Companion.point
import dev.sargunv.maplibrekmp.expression.Point
import dev.sargunv.maplibrekmp.expression.TResolvedImage
import io.github.dellisd.spatialk.geojson.Feature
import androidx.compose.runtime.key as composeKey

/**
 * A fill extrusion layer draws polygons from the [sourceLayer] in the given [source] in the
 * given style as a series of extruded polygon fills, i.e. a polygon with a certain extent on the
 * z-axis. If nothing else is specified, these 3D polygons will be black and flat.
 *
 * @param id
 *   Unique layer name.
 *
 * @param source
 *   Vector data source for this layer.
 *
 * @param sourceLayer
 *   Layer to use from the given vector tile [source].
 *
 * @param minZoom
 *   The minimum zoom level for the layer. At zoom levels less than this, the layer will be hidden.
 *   A value in the range of `[0..24]`.
 *
 * @param maxZoom
 *   The maximum zoom level for the layer. At zoom levels equal to or greater than this, the layer
 *   will be hidden. A value in the range of `[0..24]`.
 *
 * @param filter
 *   An expression specifying conditions on source features. Only features that match the filter are
 *   displayed. Zoom expressions in filters are only evaluated at integer zoom levels. The
 *   `feature-state` expression is not supported in filter expressions.
 *
 * @param visible
 *   Whether the layer should be displayed.
 *
 * @param translate
 *   The geometry's offset relative to the [translateAnchor]. Negative numbers indicate left and up
 *   (on the flat plane), respectively.
 *
 * @param translateAnchor
 *   Frame of reference for offsetting geometry, see [TranslateAnchor].
 *
 *   Ignored if [translate] is not set.
 *
 * @param opacity
 *   The opacity of the entire fill extrusion layer. This is rendered on a per-layer, not
 *   per-feature, basis, and data-driven styling is not available. A value in range `[0..1]`.
 *
 * @param color
 *   The base color of the extruded fill. The extrusion's surfaces will be shaded differently based
 *   on this color in combination with the root light settings. The alpha component of the specified
 *   color is ignored. Ignored if [pattern] is specified.
 *
 * @param pattern
 *   Name of image in sprite to use for drawing images on extruded fills. For seamless patterns,
 *   image width and height must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent
 *   expressions will be evaluated only at integer zoom levels.
 *
 * @param height
 *   The height in meters with which to extrude the geometries, i.e. the upper end of the 3D
 *   polygon. A value in the range of `[0..infinity)`.
 *
 * @param base
 *   The height in meters with which to extrude the base of the geometries, i.e. the lower end of
 *   the 3D polygon.
 *   A value in the range of `[0..infinity)`. Must be less than or equal to [height].
 *
 * @param verticalGradient
 *   Whether to apply a vertical gradient to the sides of this layer. If `true`, sides
 *   will be shaded slightly darker farther down.
 * */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun FillExtrusionLayer(
  id: String,
  source: Source,
  sourceLayer: String = "",
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  filter: Expression<Boolean> = nil(),
  visible: Boolean = true,
  translate: Expression<Point> = point(0.0, 0.0),
  translateAnchor: Expression<String> = const(TranslateAnchor.Map),
  opacity: Expression<Number> = const(1.0),
  color: Expression<Color> = const(Color.Black),
  pattern: Expression<TResolvedImage> = nil(),
  height: Expression<Number> = const(0.0),
  base: Expression<Number> = const(0.0),
  verticalGradient: Expression<Boolean> = const(true),
  noinline onClick: ((features: List<Feature>) -> Unit)? = null,
  noinline onLongClick: ((features: List<Feature>) -> Unit)? = null,
) {
  composeKey(id) {
    LayerNode(
      factory = { FillExtrusionLayer(id = id, source = source) },
      update = {
        set(sourceLayer) { layer.sourceLayer = it }
        set(minZoom) { layer.minZoom = it }
        set(maxZoom) { layer.maxZoom = it }
        set(filter) { layer.setFilter(it) }
        set(visible) { layer.visible = it }
        set(opacity) { layer.setFillExtrusionOpacity(it) }
        set(color) { layer.setFillExtrusionColor(it) }
        set(translate) { layer.setFillExtrusionTranslate(it) }
        set(translateAnchor) { layer.setFillExtrusionTranslateAnchor(it) }
        set(pattern) { layer.setFillExtrusionPattern(it) }
        set(height) { layer.setFillExtrusionHeight(it) }
        set(base) { layer.setFillExtrusionBase(it) }
        set(verticalGradient) { layer.setFillExtrusionVerticalGradient(it) }
      },
      onClick = onClick,
      onLongClick = onLongClick,
    )
  }
}
