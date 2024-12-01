package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import cocoapods.MapLibre.MLNLineStyleLayer
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TResolvedImage
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression
import dev.sargunv.maplibrecompose.core.util.toNSPredicate

@PublishedApi
internal actual class LineLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {

  override val impl = MLNLineStyleLayer(id, source.impl)

  actual override var sourceLayer: String
    get() = impl.sourceLayerIdentifier!!
    set(value) {
      impl.sourceLayerIdentifier = value
    }

  actual override fun setFilter(filter: Expression<Boolean>) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setLineCap(cap: Expression<LineCap>) {
    impl.lineCap = cap.toNSExpression()
  }

  actual fun setLineJoin(join: Expression<LineJoin>) {
    impl.lineJoin = join.toNSExpression()
  }

  actual fun setLineMiterLimit(miterLimit: Expression<Number>) {
    impl.lineMiterLimit = miterLimit.toNSExpression()
  }

  actual fun setLineRoundLimit(roundLimit: Expression<Number>) {
    impl.lineRoundLimit = roundLimit.toNSExpression()
  }

  actual fun setLineSortKey(sortKey: Expression<Number>) {
    impl.lineSortKey = sortKey.toNSExpression()
  }

  actual fun setLineOpacity(opacity: Expression<Number>) {
    impl.lineOpacity = opacity.toNSExpression()
  }

  actual fun setLineColor(color: Expression<Color>) {
    impl.lineColor = color.toNSExpression()
  }

  actual fun setLineTranslate(translate: Expression<Offset>) {
    impl.lineTranslation = translate.toNSExpression()
  }

  actual fun setLineTranslateAnchor(translateAnchor: Expression<TranslateAnchor>) {
    impl.lineTranslationAnchor = translateAnchor.toNSExpression()
  }

  actual fun setLineWidth(width: Expression<Number>) {
    impl.lineWidth = width.toNSExpression()
  }

  actual fun setLineGapWidth(gapWidth: Expression<Number>) {
    impl.lineGapWidth = gapWidth.toNSExpression()
  }

  actual fun setLineOffset(offset: Expression<Number>) {
    impl.lineOffset = offset.toNSExpression()
  }

  actual fun setLineBlur(blur: Expression<Number>) {
    impl.lineBlur = blur.toNSExpression()
  }

  actual fun setLineDasharray(dasharray: Expression<List<Number>>) {
    impl.lineDashPattern = dasharray.toNSExpression()
  }

  actual fun setLinePattern(pattern: Expression<TResolvedImage>) {
    // TODO: figure out how to unset a pattern in iOS
    if (pattern.value != null) {
      impl.linePattern = pattern.toNSExpression()
    }
  }

  actual fun setLineGradient(gradient: Expression<Color>) {
    impl.lineGradient = gradient.toNSExpression()
  }
}
