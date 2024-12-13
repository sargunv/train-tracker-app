package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNHeatmapStyleLayer
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression
import dev.sargunv.maplibrecompose.core.util.toNSPredicate

@PublishedApi
internal actual class HeatmapLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {

  override val impl = MLNHeatmapStyleLayer(id, source.impl)

  actual override var sourceLayer: String
    get() = impl.sourceLayerIdentifier!!
    set(value) {
      impl.sourceLayerIdentifier = value
    }

  actual override fun setFilter(filter: Expression.Boolean) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setHeatmapRadius(radius: Expression.Dp) {
    impl.heatmapRadius = radius.toNSExpression()
  }

  actual fun setHeatmapWeight(weight: Expression.Float) {
    impl.heatmapWeight = weight.toNSExpression()
  }

  actual fun setHeatmapIntensity(intensity: Expression.Float) {
    impl.heatmapIntensity = intensity.toNSExpression()
  }

  actual fun setHeatmapColor(color: Expression.Color) {
    impl.heatmapColor = color.toNSExpression()
  }

  actual fun setHeatmapOpacity(opacity: Expression.Float) {
    impl.heatmapOpacity = opacity.toNSExpression()
  }
}
