package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.ImageValue
import dev.sargunv.maplibrecompose.core.expression.ResolvedValue
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

internal expect class FillLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<BooleanValue>)

  fun setFillSortKey(sortKey: Expression<FloatValue>)

  fun setFillAntialias(antialias: Expression<BooleanValue>)

  fun setFillOpacity(opacity: Expression<FloatValue>)

  fun setFillColor(color: Expression<ColorValue>)

  fun setFillOutlineColor(outlineColor: Expression<ColorValue>)

  fun setFillTranslate(translate: Expression<DpOffsetValue>)

  fun setFillTranslateAnchor(translateAnchor: Expression<EnumValue<TranslateAnchor>>)

  fun setFillPattern(pattern: Expression<ResolvedValue<ImageValue>>)
}
