package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import dev.sargunv.maplibrecompose.compose.MaplibreComposable
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.const
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.MillisecondsValue
import dev.sargunv.maplibrecompose.core.expression.RasterResampling
import dev.sargunv.maplibrecompose.core.layer.RasterLayer
import dev.sargunv.maplibrecompose.core.source.Source
import kotlin.time.Duration.Companion.milliseconds

/**
 * Raster map textures such as satellite imagery.
 *
 * @param id Unique layer name.
 * @param source Raster data source for this layer.
 * @param minZoom The minimum zoom level for the layer. At zoom levels less than this, the layer
 *   will be hidden. A value in the range of `[0..24]`.
 * @param maxZoom The maximum zoom level for the layer. At zoom levels equal to or greater than
 *   this, the layer will be hidden. A value in the range of `[0..24]`.
 * @param visible Whether the layer should be displayed.
 * @param opacity The opacity at which the texture will be drawn. A value in range `[0..1]`.
 * @param hueRotate Rotates hues around the color wheel. Unit in degrees, i.e. a value in range
 *   `[0..360)`.
 * @param brightnessMin Increase or reduce the brightness of the image. The value is the minimum
 *   brightness. A value in range `[0..1]`.
 * @param brightnessMax Increase or reduce the brightness of the image. The value is the maximum
 *   brightness. A value in range `[0..1]`.
 * @param saturation Increase or reduce the saturation of the image. A value in range `[-1..1]`.
 * @param contrast Increase or reduce the contrast of the image. A value in range `[-1..1]`.
 * @param resampling The resampling/interpolation method to use for overscaling, also known as
 *   texture magnification filter.
 * @param fadeDuration Fade duration in milliseconds when a new tile is added, or when a video is
 *   started or its coordinates are updated. A value in range `[0..infinity)`.
 */
@Composable
@MaplibreComposable
public fun RasterLayer(
  id: String,
  source: Source,
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  visible: Boolean = true,
  opacity: Expression<FloatValue> = const(1f),
  hueRotate: Expression<FloatValue> = const(0f),
  brightnessMin: Expression<FloatValue> = const(0f),
  brightnessMax: Expression<FloatValue> = const(1f),
  saturation: Expression<FloatValue> = const(0f),
  contrast: Expression<FloatValue> = const(0f),
  resampling: Expression<EnumValue<RasterResampling>> = const(RasterResampling.Linear),
  fadeDuration: Expression<MillisecondsValue> = const(300.milliseconds),
) {
  LayerNode(
    factory = { RasterLayer(id = id, source = source) },
    update = {
      set(minZoom) { layer.minZoom = it }
      set(maxZoom) { layer.maxZoom = it }
      set(visible) { layer.visible = it }
      set(opacity) { layer.setRasterOpacity(it) }
      set(hueRotate) { layer.setRasterHueRotate(it) }
      set(brightnessMin) { layer.setRasterBrightnessMin(it) }
      set(brightnessMax) { layer.setRasterBrightnessMax(it) }
      set(saturation) { layer.setRasterSaturation(it) }
      set(contrast) { layer.setRasterContrast(it) }
      set(resampling) { layer.setRasterResampling(it) }
      set(fadeDuration) { layer.setRasterFadeDuration(it) }
    },
    onClick = null,
    onLongClick = null,
  )
}
