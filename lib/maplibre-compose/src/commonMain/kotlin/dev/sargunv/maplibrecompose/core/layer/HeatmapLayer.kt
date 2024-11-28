package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class HeatmapLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<Boolean>)

  fun setHeatmapRadius(radius: Expression<Dp>)

  fun setHeatmapWeight(weight: Expression<Number>)

  fun setHeatmapIntensity(intensity: Expression<Number>)

  fun setHeatmapColor(color: Expression<Color>)

  fun setHeatmapOpacity(opacity: Expression<Number>)
}
