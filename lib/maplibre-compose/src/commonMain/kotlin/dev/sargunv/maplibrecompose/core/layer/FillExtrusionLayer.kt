package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.ImageExpression
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class FillExtrusionLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: BooleanExpression)

  fun setFillExtrusionOpacity(opacity: FloatExpression)

  fun setFillExtrusionColor(color: ColorExpression)

  fun setFillExtrusionTranslate(translate: DpOffsetExpression)

  fun setFillExtrusionTranslateAnchor(anchor: EnumExpression<TranslateAnchor>)

  fun setFillExtrusionPattern(pattern: ImageExpression)

  fun setFillExtrusionHeight(height: FloatExpression)

  fun setFillExtrusionBase(base: FloatExpression)

  fun setFillExtrusionVerticalGradient(verticalGradient: BooleanExpression)
}
