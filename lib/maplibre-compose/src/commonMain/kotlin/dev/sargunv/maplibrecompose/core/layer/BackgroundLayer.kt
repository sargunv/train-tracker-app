package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression

@PublishedApi
internal expect class BackgroundLayer(id: String) : Layer {
  fun setBackgroundColor(color: Expression.Color)

  fun setBackgroundPattern(pattern: Expression.ResolvedImage)

  fun setBackgroundOpacity(opacity: Expression.Float)
}
