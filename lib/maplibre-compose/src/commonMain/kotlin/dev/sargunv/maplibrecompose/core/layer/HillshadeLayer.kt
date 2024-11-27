package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HillshadeLayer(id: String, source: Source) : Layer {
  val source: Source

  fun setHillshadeIlluminationDirection(direction: Expression<Number>)

  fun setHillshadeIlluminationAnchor(anchor: Expression<IlluminationAnchor>)

  fun setHillshadeExaggeration(exaggeration: Expression<Number>)

  fun setHillshadeShadowColor(shadowColor: Expression<Color>)

  fun setHillshadeHighlightColor(highlightColor: Expression<Color>)

  fun setHillshadeAccentColor(accentColor: Expression<Color>)
}
