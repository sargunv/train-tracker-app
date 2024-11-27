package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class RasterLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setRasterOpacity(opacity: Expression<Number>)

  fun setRasterHueRotate(hueRotate: Expression<Number>)

  fun setRasterBrightnessMin(brightnessMin: Expression<Number>)

  fun setRasterBrightnessMax(brightnessMax: Expression<Number>)

  fun setRasterSaturation(saturation: Expression<Number>)

  fun setRasterContrast(contrast: Expression<Number>)

  fun setRasterResampling(resampling: Expression<RasterResampling>)

  fun setRasterFadeDuration(fadeDuration: Expression<Number>)
}
