package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.CirclePitchAlignment
import dev.sargunv.maplibrecompose.core.expression.CirclePitchScale
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class CircleLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<BooleanValue>)

  fun setCircleSortKey(sortKey: Expression<FloatValue>)

  fun setCircleRadius(radius: Expression<DpValue>)

  fun setCircleColor(color: Expression<ColorValue>)

  fun setCircleBlur(blur: Expression<FloatValue>)

  fun setCircleOpacity(opacity: Expression<FloatValue>)

  fun setCircleTranslate(translate: Expression<DpOffsetValue>)

  fun setCircleTranslateAnchor(translateAnchor: Expression<EnumValue<TranslateAnchor>>)

  fun setCirclePitchScale(pitchScale: Expression<EnumValue<CirclePitchScale>>)

  fun setCirclePitchAlignment(pitchAlignment: Expression<EnumValue<CirclePitchAlignment>>)

  fun setCircleStrokeWidth(strokeWidth: Expression<DpValue>)

  fun setCircleStrokeColor(strokeColor: Expression<ColorValue>)

  fun setCircleStrokeOpacity(strokeOpacity: Expression<FloatValue>)
}
