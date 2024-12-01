package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.TResolvedImage
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class FillLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<Boolean>)

  fun setFillSortKey(sortKey: Expression<Number>)

  fun setFillAntialias(antialias: Expression<Boolean>)

  fun setFillOpacity(opacity: Expression<Number>)

  fun setFillColor(color: Expression<Color>)

  fun setFillOutlineColor(outlineColor: Expression<Color>)

  fun setFillTranslate(translate: Expression<DpOffset>)

  fun setFillTranslateAnchor(translateAnchor: Expression<TranslateAnchor>)

  fun setFillPattern(pattern: Expression<TResolvedImage>)
}
