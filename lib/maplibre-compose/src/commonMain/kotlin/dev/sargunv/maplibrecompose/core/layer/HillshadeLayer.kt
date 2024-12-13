package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.IlluminationAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HillshadeLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setHillshadeIlluminationDirection(direction: FloatExpression)

  fun setHillshadeIlluminationAnchor(anchor: EnumExpression<IlluminationAnchor>)

  fun setHillshadeExaggeration(exaggeration: FloatExpression)

  fun setHillshadeShadowColor(shadowColor: ColorExpression)

  fun setHillshadeHighlightColor(highlightColor: ColorExpression)

  fun setHillshadeAccentColor(accentColor: ColorExpression)
}
