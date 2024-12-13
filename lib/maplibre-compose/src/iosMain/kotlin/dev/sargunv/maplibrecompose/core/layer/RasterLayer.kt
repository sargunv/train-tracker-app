package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNRasterStyleLayer
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.RasterResampling
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression

@PublishedApi
internal actual class RasterLayer actual constructor(id: String, actual val source: Source) :
  Layer() {

  override val impl = MLNRasterStyleLayer(id, source.impl)

  actual fun setRasterOpacity(opacity: Expression.Float) {
    impl.rasterOpacity = opacity.toNSExpression()
  }

  actual fun setRasterHueRotate(hueRotate: Expression.Int) {
    impl.rasterHueRotation = hueRotate.toNSExpression()
  }

  actual fun setRasterBrightnessMin(brightnessMin: Expression.Float) {
    impl.minimumRasterBrightness = brightnessMin.toNSExpression()
  }

  actual fun setRasterBrightnessMax(brightnessMax: Expression.Float) {
    impl.maximumRasterBrightness = brightnessMax.toNSExpression()
  }

  actual fun setRasterSaturation(saturation: Expression.Float) {
    impl.rasterSaturation = saturation.toNSExpression()
  }

  actual fun setRasterContrast(contrast: Expression.Float) {
    impl.rasterContrast = contrast.toNSExpression()
  }

  actual fun setRasterResampling(resampling: Expression.Enum<RasterResampling>) {
    impl.rasterResamplingMode = resampling.toNSExpression()
  }

  actual fun setRasterFadeDuration(fadeDuration: Expression.Int) {
    impl.rasterFadeDuration = fadeDuration.toNSExpression()
  }
}
