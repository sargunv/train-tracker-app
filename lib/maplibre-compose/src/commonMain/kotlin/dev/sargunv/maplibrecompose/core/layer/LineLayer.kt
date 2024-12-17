package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.ImageValue
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.expression.VectorValue
import dev.sargunv.maplibrecompose.core.source.Source

internal expect class LineLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<BooleanValue>)

  fun setLineCap(cap: Expression<EnumValue<LineCap>>)

  fun setLineJoin(join: Expression<EnumValue<LineJoin>>)

  fun setLineMiterLimit(miterLimit: Expression<FloatValue>)

  fun setLineRoundLimit(roundLimit: Expression<FloatValue>)

  fun setLineSortKey(sortKey: Expression<FloatValue>)

  fun setLineOpacity(opacity: Expression<FloatValue>)

  fun setLineColor(color: Expression<ColorValue>)

  fun setLineTranslate(translate: Expression<DpOffsetValue>)

  fun setLineTranslateAnchor(translateAnchor: Expression<EnumValue<TranslateAnchor>>)

  fun setLineWidth(width: Expression<DpValue>)

  fun setLineGapWidth(gapWidth: Expression<DpValue>)

  fun setLineOffset(offset: Expression<DpValue>)

  fun setLineBlur(blur: Expression<DpValue>)

  fun setLineDasharray(dasharray: Expression<VectorValue<Number>>)

  fun setLinePattern(pattern: Expression<ImageValue>)

  fun setLineGradient(gradient: Expression<ColorValue>)
}
