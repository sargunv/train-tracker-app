package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.ImageExpression
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.expression.VectorExpression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class LineLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: BooleanExpression)

  fun setLineCap(cap: EnumExpression<LineCap>)

  fun setLineJoin(join: EnumExpression<LineJoin>)

  fun setLineMiterLimit(miterLimit: FloatExpression)

  fun setLineRoundLimit(roundLimit: FloatExpression)

  fun setLineSortKey(sortKey: FloatExpression)

  fun setLineOpacity(opacity: FloatExpression)

  fun setLineColor(color: ColorExpression)

  fun setLineTranslate(translate: DpOffsetExpression)

  fun setLineTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>)

  fun setLineWidth(width: DpExpression)

  fun setLineGapWidth(gapWidth: DpExpression)

  fun setLineOffset(offset: DpExpression)

  fun setLineBlur(blur: DpExpression)

  fun setLineDasharray(dasharray: VectorExpression)

  fun setLinePattern(pattern: ImageExpression)

  fun setLineGradient(gradient: ColorExpression)
}
