package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import org.maplibre.android.style.expressions.Expression as MLNExpression
import org.maplibre.android.style.layers.LineLayer as MLNLineLayer
import org.maplibre.android.style.layers.PropertyFactory

@PublishedApi
internal actual class LineLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {

  override val impl = MLNLineLayer(id, source.id)

  actual override var sourceLayer: String by impl::sourceLayer

  actual override fun setFilter(filter: Expression.Boolean) {
    impl.setFilter(filter.toMLNExpression() ?: MLNExpression.literal(true))
  }

  actual fun setLineCap(cap: Expression.Enum<LineCap>) {
    impl.setProperties(PropertyFactory.lineCap(cap.toMLNExpression()))
  }

  actual fun setLineJoin(join: Expression.Enum<LineJoin>) {
    impl.setProperties(PropertyFactory.lineJoin(join.toMLNExpression()))
  }

  actual fun setLineMiterLimit(miterLimit: Expression.Float) {
    impl.setProperties(PropertyFactory.lineMiterLimit(miterLimit.toMLNExpression()))
  }

  actual fun setLineRoundLimit(roundLimit: Expression.Float) {
    impl.setProperties(PropertyFactory.lineRoundLimit(roundLimit.toMLNExpression()))
  }

  actual fun setLineSortKey(sortKey: Expression.Float) {
    impl.setProperties(PropertyFactory.lineSortKey(sortKey.toMLNExpression()))
  }

  actual fun setLineOpacity(opacity: Expression.Float) {
    impl.setProperties(PropertyFactory.lineOpacity(opacity.toMLNExpression()))
  }

  actual fun setLineColor(color: Expression.Color) {
    impl.setProperties(PropertyFactory.lineColor(color.toMLNExpression()))
  }

  actual fun setLineTranslate(translate: Expression.DpOffset) {
    impl.setProperties(PropertyFactory.lineTranslate(translate.toMLNExpression()))
  }

  actual fun setLineTranslateAnchor(translateAnchor: Expression.Enum<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.lineTranslateAnchor(translateAnchor.toMLNExpression()))
  }

  actual fun setLineWidth(width: Expression.Dp) {
    impl.setProperties(PropertyFactory.lineWidth(width.toMLNExpression()))
  }

  actual fun setLineGapWidth(gapWidth: Expression.Dp) {
    impl.setProperties(PropertyFactory.lineGapWidth(gapWidth.toMLNExpression()))
  }

  actual fun setLineOffset(offset: Expression.Dp) {
    impl.setProperties(PropertyFactory.lineOffset(offset.toMLNExpression()))
  }

  actual fun setLineBlur(blur: Expression.Dp) {
    impl.setProperties(PropertyFactory.lineBlur(blur.toMLNExpression()))
  }

  actual fun setLineDasharray(dasharray: Expression.Vector) {
    impl.setProperties(PropertyFactory.lineDasharray(dasharray.toMLNExpression()))
  }

  actual fun setLinePattern(pattern: Expression.ResolvedImage) {
    impl.setProperties(PropertyFactory.linePattern(pattern.toMLNExpression()))
  }

  actual fun setLineGradient(gradient: Expression.Color) {
    impl.setProperties(PropertyFactory.lineGradient(gradient.toMLNExpression()))
  }
}
