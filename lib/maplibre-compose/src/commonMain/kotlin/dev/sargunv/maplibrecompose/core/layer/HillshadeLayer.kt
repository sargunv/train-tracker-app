package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.IlluminationAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HillshadeLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setHillshadeIlluminationDirection(direction: Expression.Int)

  fun setHillshadeIlluminationAnchor(anchor: Expression.Enum<IlluminationAnchor>)

  fun setHillshadeExaggeration(exaggeration: Expression.Float)

  fun setHillshadeShadowColor(shadowColor: Expression.Color)

  fun setHillshadeHighlightColor(highlightColor: Expression.Color)

  fun setHillshadeAccentColor(accentColor: Expression.Color)
}
