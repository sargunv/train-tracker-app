package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HeatmapLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<BooleanValue>)

  fun setHeatmapRadius(radius: Expression<DpValue>)

  fun setHeatmapWeight(weight: Expression<FloatValue>)

  fun setHeatmapIntensity(intensity: Expression<FloatValue>)

  fun setHeatmapColor(color: Expression<ColorValue>)

  fun setHeatmapOpacity(opacity: Expression<FloatValue>)
}
