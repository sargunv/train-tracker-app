package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNHeatmapStyleLayer
import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression
import dev.sargunv.maplibrecompose.core.util.toNSPredicate

internal actual class HeatmapLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {

  override val impl = MLNHeatmapStyleLayer(id, source.impl)

  actual override var sourceLayer: String
    get() = impl.sourceLayerIdentifier!!
    set(value) {
      impl.sourceLayerIdentifier = value
    }

  actual override fun setFilter(filter: Expression<BooleanValue>) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setHeatmapRadius(radius: Expression<DpValue>) {
    impl.heatmapRadius = radius.toNSExpression()
  }

  actual fun setHeatmapWeight(weight: Expression<FloatValue>) {
    impl.heatmapWeight = weight.toNSExpression()
  }

  actual fun setHeatmapIntensity(intensity: Expression<FloatValue>) {
    impl.heatmapIntensity = intensity.toNSExpression()
  }

  actual fun setHeatmapColor(color: Expression<ColorValue>) {
    impl.heatmapColor = color.toNSExpression()
  }

  actual fun setHeatmapOpacity(opacity: Expression<FloatValue>) {
    impl.heatmapOpacity = opacity.toNSExpression()
  }
}
