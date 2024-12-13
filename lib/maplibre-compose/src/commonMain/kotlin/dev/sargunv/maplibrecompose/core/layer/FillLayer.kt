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
internal expect class FillLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: BooleanExpression)

  fun setFillSortKey(sortKey: FloatExpression)

  fun setFillAntialias(antialias: BooleanExpression)

  fun setFillOpacity(opacity: FloatExpression)

  fun setFillColor(color: ColorExpression)

  fun setFillOutlineColor(outlineColor: ColorExpression)

  fun setFillTranslate(translate: DpOffsetExpression)

  fun setFillTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>)

  fun setFillPattern(pattern: ImageExpression)
}
