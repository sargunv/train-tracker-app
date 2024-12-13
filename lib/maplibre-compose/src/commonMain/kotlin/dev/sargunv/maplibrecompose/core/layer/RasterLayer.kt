package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.IntExpression
import dev.sargunv.maplibrecompose.core.expression.RasterResampling
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class RasterLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setRasterOpacity(opacity: FloatExpression)

  fun setRasterHueRotate(hueRotate: IntExpression)

  fun setRasterBrightnessMin(brightnessMin: FloatExpression)

  fun setRasterBrightnessMax(brightnessMax: FloatExpression)

  fun setRasterSaturation(saturation: FloatExpression)

  fun setRasterContrast(contrast: FloatExpression)

  fun setRasterResampling(resampling: EnumExpression<RasterResampling>)

  fun setRasterFadeDuration(fadeDuration: IntExpression)
}
