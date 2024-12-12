package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNBackgroundStyleLayer
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.util.toNSExpression

@PublishedApi
internal actual class BackgroundLayer actual constructor(id: String) : Layer() {
  override val impl = MLNBackgroundStyleLayer(id)

  actual fun setBackgroundColor(color: Expression.Color) {
    impl.backgroundColor = color.toNSExpression()
  }

  actual fun setBackgroundPattern(pattern: Expression.ResolvedImage) {
    // TODO: figure out how to unset a pattern in iOS
    if (pattern.value != null) {
      impl.backgroundPattern = pattern.toNSExpression()
    }
  }

  actual fun setBackgroundOpacity(opacity: Expression.Number) {
    impl.backgroundOpacity = opacity.toNSExpression()
  }
}
