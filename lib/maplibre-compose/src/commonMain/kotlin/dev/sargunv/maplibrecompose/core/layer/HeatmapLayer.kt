package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HeatmapLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: BooleanExpression)

  fun setHeatmapRadius(radius: DpExpression)

  fun setHeatmapWeight(weight: FloatExpression)

  fun setHeatmapIntensity(intensity: FloatExpression)

  fun setHeatmapColor(color: ColorExpression)

  fun setHeatmapOpacity(opacity: FloatExpression)
}
