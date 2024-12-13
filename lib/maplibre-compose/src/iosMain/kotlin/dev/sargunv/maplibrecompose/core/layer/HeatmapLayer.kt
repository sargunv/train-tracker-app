package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNHeatmapStyleLayer
import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
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

  actual override fun setFilter(filter: BooleanExpression) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setHeatmapRadius(radius: DpExpression) {
    impl.heatmapRadius = radius.toNSExpression()
  }

  actual fun setHeatmapWeight(weight: FloatExpression) {
    impl.heatmapWeight = weight.toNSExpression()
  }

  actual fun setHeatmapIntensity(intensity: FloatExpression) {
    impl.heatmapIntensity = intensity.toNSExpression()
  }

  actual fun setHeatmapColor(color: ColorExpression) {
    impl.heatmapColor = color.toNSExpression()
  }

  actual fun setHeatmapOpacity(opacity: FloatExpression) {
    impl.heatmapOpacity = opacity.toNSExpression()
  }
}
