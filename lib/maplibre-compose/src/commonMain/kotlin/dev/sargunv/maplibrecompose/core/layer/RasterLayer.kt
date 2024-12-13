package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.RasterResampling
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class RasterLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setRasterOpacity(opacity: Expression.Float)

  fun setRasterHueRotate(hueRotate: Expression.Int)

  fun setRasterBrightnessMin(brightnessMin: Expression.Float)

  fun setRasterBrightnessMax(brightnessMax: Expression.Float)

  fun setRasterSaturation(saturation: Expression.Float)

  fun setRasterContrast(contrast: Expression.Float)

  fun setRasterResampling(resampling: Expression.Enum<RasterResampling>)

  fun setRasterFadeDuration(fadeDuration: Expression.Int)
}
