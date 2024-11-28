package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import org.maplibre.android.style.expressions.Expression as MLNExpression
import org.maplibre.android.style.layers.HeatmapLayer as MLNHeatmapLayer
import org.maplibre.android.style.layers.PropertyFactory

@PublishedApi
internal actual class HeatmapLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {
  override val impl = MLNHeatmapLayer(id, source.id)

  actual override var sourceLayer: String by impl::sourceLayer

  actual override fun setFilter(filter: Expression<Boolean>) {
    impl.setFilter(filter.toMLNExpression() ?: MLNExpression.literal(true))
  }

  actual fun setHeatmapRadius(radius: Expression<Dp>) {
    impl.setProperties(PropertyFactory.heatmapRadius(radius.toMLNExpression()))
  }

  actual fun setHeatmapWeight(weight: Expression<Number>) {
    impl.setProperties(PropertyFactory.heatmapWeight(weight.toMLNExpression()))
  }

  actual fun setHeatmapIntensity(intensity: Expression<Number>) {
    impl.setProperties(PropertyFactory.heatmapIntensity(intensity.toMLNExpression()))
  }

  actual fun setHeatmapColor(color: Expression<Color>) {
    impl.setProperties(PropertyFactory.heatmapColor(color.toMLNExpression()))
  }

  actual fun setHeatmapOpacity(opacity: Expression<Number>) {
    impl.setProperties(PropertyFactory.heatmapOpacity(opacity.toMLNExpression()))
  }
}
