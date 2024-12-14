package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.ImageValue

@PublishedApi
internal expect class BackgroundLayer(id: String) : Layer {
  fun setBackgroundColor(color: Expression<ColorValue>)

  fun setBackgroundPattern(pattern: Expression<ImageValue>)

  fun setBackgroundOpacity(opacity: Expression<FloatValue>)
}
