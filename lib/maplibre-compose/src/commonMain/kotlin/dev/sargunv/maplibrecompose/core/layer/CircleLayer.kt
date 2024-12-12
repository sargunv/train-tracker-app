package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.CirclePitchAlignment
import dev.sargunv.maplibrecompose.core.expression.CirclePitchScale
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class CircleLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression.Boolean)

  fun setCircleSortKey(sortKey: Expression.Number)

  fun setCircleRadius(radius: Expression.Dp)

  fun setCircleColor(color: Expression.Color)

  fun setCircleBlur(blur: Expression.Number)

  fun setCircleOpacity(opacity: Expression.Number)

  fun setCircleTranslate(translate: Expression.DpOffset)

  fun setCircleTranslateAnchor(translateAnchor: Expression.Enum<TranslateAnchor>)

  fun setCirclePitchScale(pitchScale: Expression.Enum<CirclePitchScale>)

  fun setCirclePitchAlignment(pitchAlignment: Expression.Enum<CirclePitchAlignment>)

  fun setCircleStrokeWidth(strokeWidth: Expression.Dp)

  fun setCircleStrokeColor(strokeColor: Expression.Color)

  fun setCircleStrokeOpacity(strokeOpacity: Expression.Number)
}
