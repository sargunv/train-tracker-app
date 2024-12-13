package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.CirclePitchAlignment
import dev.sargunv.maplibrecompose.core.expression.CirclePitchScale
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class CircleLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: BooleanExpression)

  fun setCircleSortKey(sortKey: FloatExpression)

  fun setCircleRadius(radius: DpExpression)

  fun setCircleColor(color: ColorExpression)

  fun setCircleBlur(blur: FloatExpression)

  fun setCircleOpacity(opacity: FloatExpression)

  fun setCircleTranslate(translate: DpOffsetExpression)

  fun setCircleTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>)

  fun setCirclePitchScale(pitchScale: EnumExpression<CirclePitchScale>)

  fun setCirclePitchAlignment(pitchAlignment: EnumExpression<CirclePitchAlignment>)

  fun setCircleStrokeWidth(strokeWidth: DpExpression)

  fun setCircleStrokeColor(strokeColor: ColorExpression)

  fun setCircleStrokeOpacity(strokeOpacity: FloatExpression)
}
