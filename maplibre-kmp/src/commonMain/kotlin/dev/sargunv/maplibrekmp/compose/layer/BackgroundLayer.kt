package dev.sargunv.maplibrekmp.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrekmp.core.layer.BackgroundLayer
import dev.sargunv.maplibrekmp.expression.Expression
import dev.sargunv.maplibrekmp.expression.Expression.Companion.const
import dev.sargunv.maplibrekmp.expression.Expression.Companion.nil
import dev.sargunv.maplibrekmp.expression.TResolvedImage
import androidx.compose.runtime.key as composeKey

/**
 * The background layer just draws the map background, by default, plain black.
 *
 * @param id
 *   Unique layer name.
 *
 * @param minZoom
 *   The minimum zoom level for the layer. At zoom levels less than this, the layer will be hidden.
 *   A value in the range of `[0..24]`.
 *
 * @param maxZoom
 *   The maximum zoom level for the layer. At zoom levels equal to or greater than this, the layer
 *   will be hidden. A value in the range of `[0..24]`.
 *
 * @param visible
 *   Whether the layer should be displayed.
 *
 * @param color
 *   Background color.
 *
 *   Ignored if [pattern] is specified.
 *
 * @param pattern
 *   Image to use for drawing image fills. For seamless patterns, image width and height must be a
 *   factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only
 *   at integer zoom levels.
 *
 * @param opacity
 *   Background opacity. A value in range `[0..1]`.
 *
 * */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun BackgroundLayer(
  id: String,
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  visible: Boolean = true,
  color: Expression<Color> = const(Color.Black),
  pattern: Expression<TResolvedImage> = nil(),
  opacity: Expression<Number> = const(1),
) {
  composeKey(id) {
    LayerNode(
      factory = { BackgroundLayer(id = id) },
      update = {
        set(minZoom) { layer.minZoom = it }
        set(maxZoom) { layer.maxZoom = it }
        set(visible) { layer.visible = it }
        set(color) { layer.setBackgroundColor(it) }
        set(pattern) { layer.setBackgroundPattern(it) }
        set(opacity) { layer.setBackgroundOpacity(it) }
      },
      onClick = null,
      onLongClick = null,
    )
  }
}
