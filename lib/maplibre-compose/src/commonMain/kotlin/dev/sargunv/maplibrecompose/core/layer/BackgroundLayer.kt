package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.ImageExpression

@PublishedApi
internal expect class BackgroundLayer(id: String) : Layer {
  fun setBackgroundColor(color: ColorExpression)

  fun setBackgroundPattern(pattern: ImageExpression)

  fun setBackgroundOpacity(opacity: FloatExpression)
}
