package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.CirclePitchAlignment
import dev.sargunv.maplibrecompose.core.expression.CirclePitchScale
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
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

  actual override fun setFilter(filter: BooleanExpression) {
    impl.setFilter(filter.toMLNExpression() ?: MLNExpression.literal(true))
  }

  actual fun setCircleSortKey(sortKey: FloatExpression) {
    impl.setProperties(PropertyFactory.circleSortKey(sortKey.toMLNExpression()))
  }

  actual fun setCircleRadius(radius: DpExpression) {
    impl.setProperties(PropertyFactory.circleRadius(radius.toMLNExpression()))
  }

  actual fun setCircleColor(color: ColorExpression) {
    impl.setProperties(PropertyFactory.circleColor(color.toMLNExpression()))
  }

  actual fun setCircleBlur(blur: FloatExpression) {
    impl.setProperties(PropertyFactory.circleBlur(blur.toMLNExpression()))
  }

  actual fun setCircleOpacity(opacity: FloatExpression) {
    impl.setProperties(PropertyFactory.circleOpacity(opacity.toMLNExpression()))
  }

  actual fun setCircleTranslate(translate: DpOffsetExpression) {
    impl.setProperties(PropertyFactory.circleTranslate(translate.toMLNExpression()))
  }

  actual fun setCircleTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.circleTranslateAnchor(translateAnchor.toMLNExpression()))
  }

  actual fun setCirclePitchScale(pitchScale: EnumExpression<CirclePitchScale>) {
    impl.setProperties(PropertyFactory.circlePitchScale(pitchScale.toMLNExpression()))
  }

  actual fun setCirclePitchAlignment(pitchAlignment: EnumExpression<CirclePitchAlignment>) {
    impl.setProperties(PropertyFactory.circlePitchAlignment(pitchAlignment.toMLNExpression()))
  }

  actual fun setCircleStrokeWidth(strokeWidth: DpExpression) {
    impl.setProperties(PropertyFactory.circleStrokeWidth(strokeWidth.toMLNExpression()))
  }

  actual fun setCircleStrokeColor(strokeColor: ColorExpression) {
    impl.setProperties(PropertyFactory.circleStrokeColor(strokeColor.toMLNExpression()))
  }

  actual fun setCircleStrokeOpacity(strokeOpacity: FloatExpression) {
    impl.setProperties(PropertyFactory.circleStrokeOpacity(strokeOpacity.toMLNExpression()))
  }
}
