package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.IlluminationAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HillshadeLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setHillshadeIlluminationDirection(direction: Expression<FloatValue>)

  fun setHillshadeIlluminationAnchor(anchor: Expression<EnumValue<IlluminationAnchor>>)

  fun setHillshadeExaggeration(exaggeration: Expression<FloatValue>)

  fun setHillshadeShadowColor(shadowColor: Expression<ColorValue>)

  fun setHillshadeHighlightColor(highlightColor: Expression<ColorValue>)

  fun setHillshadeAccentColor(accentColor: Expression<ColorValue>)
}
