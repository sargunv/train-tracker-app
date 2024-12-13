package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNLineStyleLayer
import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.ImageExpression
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.expression.VectorExpression
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

  actual override fun setFilter(filter: BooleanExpression) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setLineCap(cap: EnumExpression<LineCap>) {
    impl.lineCap = cap.toNSExpression()
  }

  actual fun setLineJoin(join: EnumExpression<LineJoin>) {
    impl.lineJoin = join.toNSExpression()
  }

  actual fun setLineMiterLimit(miterLimit: FloatExpression) {
    impl.lineMiterLimit = miterLimit.toNSExpression()
  }

  actual fun setLineRoundLimit(roundLimit: FloatExpression) {
    impl.lineRoundLimit = roundLimit.toNSExpression()
  }

  actual fun setLineSortKey(sortKey: FloatExpression) {
    impl.lineSortKey = sortKey.toNSExpression()
  }

  actual fun setLineOpacity(opacity: FloatExpression) {
    impl.lineOpacity = opacity.toNSExpression()
  }

  actual fun setLineColor(color: ColorExpression) {
    impl.lineColor = color.toNSExpression()
  }

  actual fun setLineTranslate(translate: DpOffsetExpression) {
    impl.lineTranslation = translate.toNSExpression()
  }

  actual fun setLineTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>) {
    impl.lineTranslationAnchor = translateAnchor.toNSExpression()
  }

  actual fun setLineWidth(width: DpExpression) {
    impl.lineWidth = width.toNSExpression()
  }

  actual fun setLineGapWidth(gapWidth: DpExpression) {
    impl.lineGapWidth = gapWidth.toNSExpression()
  }

  actual fun setLineOffset(offset: DpExpression) {
    impl.lineOffset = offset.toNSExpression()
  }

  actual fun setLineBlur(blur: DpExpression) {
    impl.lineBlur = blur.toNSExpression()
  }

  actual fun setLineDasharray(dasharray: VectorExpression) {
    impl.lineDashPattern = dasharray.toNSExpression()
  }

  actual fun setLinePattern(pattern: ImageExpression) {
    // TODO: figure out how to unset a pattern in iOS
    if (pattern.value != null) {
      impl.linePattern = pattern.toNSExpression()
    }
  }

  actual fun setLineGradient(gradient: ColorExpression) {
    impl.lineGradient = gradient.toNSExpression()
  }
}
