package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.ExpressionScope.const
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.IlluminationAnchor
import dev.sargunv.maplibrecompose.core.layer.HillshadeLayer
import dev.sargunv.maplibrecompose.core.source.Source

/**
 * Client-side hillshading visualization based on DEM data. The implementation supports Mapbox
 * Terrain RGB, Mapzen Terrarium tiles and custom encodings.
 *
 * @param id Unique layer name.
 * @param source Vector data source for this layer.
 * @param minZoom The minimum zoom level for the layer. At zoom levels less than this, the layer
 *   will be hidden. A value in the range of `[0..24]`.
 * @param maxZoom The maximum zoom level for the layer. At zoom levels equal to or greater than
 *   this, the layer will be hidden. A value in the range of `[0..24]`.
 * @param visible Whether the layer should be displayed.
 * @param shadowColor The shading color of areas that face away from the light source.
 * @param highlightColor The shading color of areas that faces towards the light source.
 * @param accentColor The shading color used to accentuate rugged terrain like sharp cliffs and
 *   gorges.
 * @param illuminationDirection The direction of the light source used to generate the hillshading
 *   in degrees. A value in the range of `[0..360)`. `0` means
 *     - the top of the viewport if [illuminationAnchor] = [IlluminationAnchor.Viewport] or
 *     - north if [illuminationAnchor] = [IlluminationAnchor.Map]
 *
 * @param illuminationAnchor Direction of light source when map is rotated. See
 *   [illuminationDirection].
 * @param exaggeration Intensity of the hillshade. A value in the range of `[0..1]`.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun HillshadeLayer(
  id: String,
  source: Source,
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  visible: Boolean = true,
  shadowColor: Expression<ColorValue> = const(Color.Black),
  highlightColor: Expression<ColorValue> = const(Color.White),
  accentColor: Expression<ColorValue> = const(Color.Black),
  illuminationDirection: Expression<FloatValue> = const(355f),
  illuminationAnchor: Expression<EnumValue<IlluminationAnchor>> =
    const(IlluminationAnchor.Viewport),
  exaggeration: Expression<FloatValue> = const(0.5f),
) {
  key(id) {
    LayerNode(
      factory = { HillshadeLayer(id = id, source = source) },
      update = {
        set(minZoom) { layer.minZoom = it }
        set(maxZoom) { layer.maxZoom = it }
        set(visible) { layer.visible = it }
        set(illuminationDirection) { layer.setHillshadeIlluminationDirection(it) }
        set(illuminationAnchor) { layer.setHillshadeIlluminationAnchor(it) }
        set(exaggeration) { layer.setHillshadeExaggeration(it) }
        set(shadowColor) { layer.setHillshadeShadowColor(it) }
        set(highlightColor) { layer.setHillshadeHighlightColor(it) }
        set(accentColor) { layer.setHillshadeAccentColor(it) }
      },
      onClick = null,
      onLongClick = null,
    )
  }
}
