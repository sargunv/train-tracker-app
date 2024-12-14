package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNHillshadeStyleLayer
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.IlluminationAnchor
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression

@PublishedApi
internal actual class HillshadeLayer actual constructor(id: String, actual val source: Source) :
  Layer() {

  override val impl = MLNHillshadeStyleLayer(id, source.impl)

  actual fun setHillshadeIlluminationDirection(direction: Expression<FloatValue>) {
    impl.hillshadeIlluminationDirection = direction.toNSExpression()
  }

  actual fun setHillshadeIlluminationAnchor(anchor: Expression<EnumValue<IlluminationAnchor>>) {
    impl.hillshadeIlluminationAnchor = anchor.toNSExpression()
  }

  actual fun setHillshadeExaggeration(exaggeration: Expression<FloatValue>) {
    impl.hillshadeExaggeration = exaggeration.toNSExpression()
  }

  actual fun setHillshadeShadowColor(shadowColor: Expression<ColorValue>) {
    impl.hillshadeShadowColor = shadowColor.toNSExpression()
  }

  actual fun setHillshadeHighlightColor(highlightColor: Expression<ColorValue>) {
    impl.hillshadeHighlightColor = highlightColor.toNSExpression()
  }

  actual fun setHillshadeAccentColor(accentColor: Expression<ColorValue>) {
    impl.hillshadeAccentColor = accentColor.toNSExpression()
  }
}
