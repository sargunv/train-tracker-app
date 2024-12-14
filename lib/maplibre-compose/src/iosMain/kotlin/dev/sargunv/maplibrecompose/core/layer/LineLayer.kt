package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNLineStyleLayer
import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.ImageValue
import dev.sargunv.maplibrecompose.core.expression.LineCap
import dev.sargunv.maplibrecompose.core.expression.LineJoin
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.expression.VectorValue
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

  actual override fun setFilter(filter: Expression<BooleanValue>) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setLineCap(cap: Expression<EnumValue<LineCap>>) {
    impl.lineCap = cap.toNSExpression()
  }

  actual fun setLineJoin(join: Expression<EnumValue<LineJoin>>) {
    impl.lineJoin = join.toNSExpression()
  }

  actual fun setLineMiterLimit(miterLimit: Expression<FloatValue>) {
    impl.lineMiterLimit = miterLimit.toNSExpression()
  }

  actual fun setLineRoundLimit(roundLimit: Expression<FloatValue>) {
    impl.lineRoundLimit = roundLimit.toNSExpression()
  }

  actual fun setLineSortKey(sortKey: Expression<FloatValue>) {
    impl.lineSortKey = sortKey.toNSExpression()
  }

  actual fun setLineOpacity(opacity: Expression<FloatValue>) {
    impl.lineOpacity = opacity.toNSExpression()
  }

  actual fun setLineColor(color: Expression<ColorValue>) {
    impl.lineColor = color.toNSExpression()
  }

  actual fun setLineTranslate(translate: Expression<DpOffsetValue>) {
    impl.lineTranslation = translate.toNSExpression()
  }

  actual fun setLineTranslateAnchor(translateAnchor: Expression<EnumValue<TranslateAnchor>>) {
    impl.lineTranslationAnchor = translateAnchor.toNSExpression()
  }

  actual fun setLineWidth(width: Expression<DpValue>) {
    impl.lineWidth = width.toNSExpression()
  }

  actual fun setLineGapWidth(gapWidth: Expression<DpValue>) {
    impl.lineGapWidth = gapWidth.toNSExpression()
  }

  actual fun setLineOffset(offset: Expression<DpValue>) {
    impl.lineOffset = offset.toNSExpression()
  }

  actual fun setLineBlur(blur: Expression<DpValue>) {
    impl.lineBlur = blur.toNSExpression()
  }

  actual fun setLineDasharray(dasharray: Expression<VectorValue<Number>>) {
    impl.lineDashPattern = dasharray.toNSExpression()
  }

  actual fun setLinePattern(pattern: Expression<ImageValue>) {
    // TODO: figure out how to unset a pattern in iOS
    if (pattern.value != null) {
      impl.linePattern = pattern.toNSExpression()
    }
  }

  actual fun setLineGradient(gradient: Expression<ColorValue>) {
    impl.lineGradient = gradient.toNSExpression()
  }
}
