package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class FillExtrusionLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression.Boolean)

  fun setFillExtrusionOpacity(opacity: Expression.Number)

  fun setFillExtrusionColor(color: Expression.Color)

  fun setFillExtrusionTranslate(translate: Expression.DpOffset)

  fun setFillExtrusionTranslateAnchor(anchor: Expression.Enum<TranslateAnchor>)

  fun setFillExtrusionPattern(pattern: Expression.ResolvedImage)

  fun setFillExtrusionHeight(height: Expression.Number)

  fun setFillExtrusionBase(base: Expression.Number)

  fun setFillExtrusionVerticalGradient(verticalGradient: Expression.Boolean)
}
