package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HeatmapLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression.Boolean)

  fun setHeatmapRadius(radius: Expression.Dp)

  fun setHeatmapWeight(weight: Expression.Float)

  fun setHeatmapIntensity(intensity: Expression.Float)

  fun setHeatmapColor(color: Expression.Color)

  fun setHeatmapOpacity(opacity: Expression.Float)
}
