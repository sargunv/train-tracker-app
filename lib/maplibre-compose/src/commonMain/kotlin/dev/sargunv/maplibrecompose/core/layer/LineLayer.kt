package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class LineLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression.Boolean)

  fun setLineCap(cap: Expression.Enum<LineCap>)

  fun setLineJoin(join: Expression.Enum<LineJoin>)

  fun setLineMiterLimit(miterLimit: Expression.Float)

  fun setLineRoundLimit(roundLimit: Expression.Float)

  fun setLineSortKey(sortKey: Expression.Float)

  fun setLineOpacity(opacity: Expression.Float)

  fun setLineColor(color: Expression.Color)

  fun setLineTranslate(translate: Expression.DpOffset)

  fun setLineTranslateAnchor(translateAnchor: Expression.Enum<TranslateAnchor>)

  fun setLineWidth(width: Expression.Dp)

  fun setLineGapWidth(gapWidth: Expression.Dp)

  fun setLineOffset(offset: Expression.Dp)

  fun setLineBlur(blur: Expression.Dp)

  fun setLineDasharray(dasharray: Expression.Vector)

  fun setLinePattern(pattern: Expression.ResolvedImage)

  fun setLineGradient(gradient: Expression.Color)
}
