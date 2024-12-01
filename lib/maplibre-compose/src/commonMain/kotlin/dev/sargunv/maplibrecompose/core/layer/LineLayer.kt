package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TResolvedImage
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class LineLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<Boolean>)

  fun setLineCap(cap: Expression<LineCap>)

  fun setLineJoin(join: Expression<LineJoin>)

  fun setLineMiterLimit(miterLimit: Expression<Number>)

  fun setLineRoundLimit(roundLimit: Expression<Number>)

  fun setLineSortKey(sortKey: Expression<Number>)

  fun setLineOpacity(opacity: Expression<Number>)

  fun setLineColor(color: Expression<Color>)

  fun setLineTranslate(translate: Expression<DpOffset>)

  fun setLineTranslateAnchor(translateAnchor: Expression<TranslateAnchor>)

  fun setLineWidth(width: Expression<Dp>)

  fun setLineGapWidth(gapWidth: Expression<Dp>)

  fun setLineOffset(offset: Expression<Dp>)

  fun setLineBlur(blur: Expression<Dp>)

  fun setLineDasharray(dasharray: Expression<List<Number>>)

  fun setLinePattern(pattern: Expression<TResolvedImage>)

  fun setLineGradient(gradient: Expression<Color>)
}
