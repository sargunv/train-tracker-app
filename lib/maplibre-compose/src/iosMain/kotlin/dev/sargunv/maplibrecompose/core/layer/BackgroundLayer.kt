package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNBackgroundStyleLayer
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.ImageExpression
import dev.sargunv.maplibrecompose.core.util.toNSExpression

@PublishedApi
internal actual class BackgroundLayer actual constructor(id: String) : Layer() {
  override val impl = MLNBackgroundStyleLayer(id)

  actual fun setBackgroundColor(color: ColorExpression) {
    impl.backgroundColor = color.toNSExpression()
  }

  actual fun setBackgroundPattern(pattern: ImageExpression) {
    // TODO: figure out how to unset a pattern in iOS
    if (pattern.value != null) {
      impl.backgroundPattern = pattern.toNSExpression()
    }
  }

  actual fun setBackgroundOpacity(opacity: FloatExpression) {
    impl.backgroundOpacity = opacity.toNSExpression()
  }
}
