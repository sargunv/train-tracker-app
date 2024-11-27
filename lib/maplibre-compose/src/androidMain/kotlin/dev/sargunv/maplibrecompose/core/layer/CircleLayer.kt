package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.Point
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import org.maplibre.android.style.expressions.Expression as MLNExpression
import org.maplibre.android.style.layers.CircleLayer as MLNCircleLayer
import org.maplibre.android.style.layers.PropertyFactory

@PublishedApi
internal actual class CircleLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {
  override val impl = MLNCircleLayer(id, source.id)

  actual override var sourceLayer: String by impl::sourceLayer

  actual override fun setFilter(filter: Expression<Boolean>) {
    impl.setFilter(filter.toMLNExpression() ?: MLNExpression.literal(true))
  }

  actual fun setCircleSortKey(sortKey: Expression<Number>) {
    impl.setProperties(PropertyFactory.circleSortKey(sortKey.toMLNExpression()))
  }

  actual fun setCircleRadius(radius: Expression<Number>) {
    impl.setProperties(PropertyFactory.circleRadius(radius.toMLNExpression()))
  }

  actual fun setCircleColor(color: Expression<Color>) {
    impl.setProperties(PropertyFactory.circleColor(color.toMLNExpression()))
  }

  actual fun setCircleBlur(blur: Expression<Number>) {
    impl.setProperties(PropertyFactory.circleBlur(blur.toMLNExpression()))
  }

  actual fun setCircleOpacity(opacity: Expression<Number>) {
    impl.setProperties(PropertyFactory.circleOpacity(opacity.toMLNExpression()))
  }

  actual fun setCircleTranslate(translate: Expression<Point>) {
    impl.setProperties(PropertyFactory.circleTranslate(translate.toMLNExpression()))
  }

  actual fun setCircleTranslateAnchor(translateAnchor: Expression<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.circleTranslateAnchor(translateAnchor.toMLNExpression()))
  }

  actual fun setCirclePitchScale(pitchScale: Expression<CirclePitchScale>) {
    impl.setProperties(PropertyFactory.circlePitchScale(pitchScale.toMLNExpression()))
  }

  actual fun setCirclePitchAlignment(pitchAlignment: Expression<CirclePitchAlignment>) {
    impl.setProperties(PropertyFactory.circlePitchAlignment(pitchAlignment.toMLNExpression()))
  }

  actual fun setCircleStrokeWidth(strokeWidth: Expression<Number>) {
    impl.setProperties(PropertyFactory.circleStrokeWidth(strokeWidth.toMLNExpression()))
  }

  actual fun setCircleStrokeColor(strokeColor: Expression<Color>) {
    impl.setProperties(PropertyFactory.circleStrokeColor(strokeColor.toMLNExpression()))
  }

  actual fun setCircleStrokeOpacity(strokeOpacity: Expression<Number>) {
    impl.setProperties(PropertyFactory.circleStrokeOpacity(strokeOpacity.toMLNExpression()))
  }
}
