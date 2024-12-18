package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.MillisecondsValue
import dev.sargunv.maplibrecompose.core.expression.RasterResampling
import dev.sargunv.maplibrecompose.core.source.Source

internal expect class RasterLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setRasterOpacity(opacity: Expression<FloatValue>)

  fun setRasterHueRotate(hueRotate: Expression<FloatValue>)

  fun setRasterBrightnessMin(brightnessMin: Expression<FloatValue>)

  fun setRasterBrightnessMax(brightnessMax: Expression<FloatValue>)

  fun setRasterSaturation(saturation: Expression<FloatValue>)

  fun setRasterContrast(contrast: Expression<FloatValue>)

  fun setRasterResampling(resampling: Expression<EnumValue<RasterResampling>>)

  fun setRasterFadeDuration(fadeDuration: Expression<MillisecondsValue>)
}
