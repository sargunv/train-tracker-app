package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import cocoapods.MapLibre.MLNFillExtrusionStyleLayer
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.Point
import dev.sargunv.maplibrecompose.core.expression.TResolvedImage
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression
import dev.sargunv.maplibrecompose.core.util.toNSPredicate

@PublishedApi
internal actual class FillExtrusionLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {

  override val impl = MLNFillExtrusionStyleLayer(id, source.impl)

  actual override var sourceLayer: String
    get() = impl.sourceLayerIdentifier!!
    set(value) {
      impl.sourceLayerIdentifier = value
    }

  actual override fun setFilter(filter: Expression<Boolean>) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setFillExtrusionOpacity(opacity: Expression<Number>) {
    impl.fillExtrusionOpacity = opacity.toNSExpression()
  }

  actual fun setFillExtrusionColor(color: Expression<Color>) {
    impl.fillExtrusionColor = color.toNSExpression()
  }

  actual fun setFillExtrusionTranslate(translate: Expression<Point>) {
    impl.fillExtrusionTranslation = translate.toNSExpression()
  }

  actual fun setFillExtrusionTranslateAnchor(anchor: Expression<String>) {
    impl.fillExtrusionTranslationAnchor = anchor.toNSExpression()
  }

  actual fun setFillExtrusionPattern(pattern: Expression<TResolvedImage>) {
    // TODO figure out how to unset pattern
    if (pattern.value != null) impl.fillExtrusionPattern = pattern.toNSExpression()
  }

  actual fun setFillExtrusionHeight(height: Expression<Number>) {
    impl.fillExtrusionHeight = height.toNSExpression()
  }

  actual fun setFillExtrusionBase(base: Expression<Number>) {
    impl.fillExtrusionBase = base.toNSExpression()
  }

  actual fun setFillExtrusionVerticalGradient(verticalGradient: Expression<Boolean>) {
    impl.fillExtrusionHasVerticalGradient = verticalGradient.toNSExpression()
  }
}
