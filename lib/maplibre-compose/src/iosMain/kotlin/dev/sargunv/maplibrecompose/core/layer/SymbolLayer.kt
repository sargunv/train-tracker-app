package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import cocoapods.MapLibre.MLNSymbolStyleLayer
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.Insets
import dev.sargunv.maplibrecompose.core.expression.Point
import dev.sargunv.maplibrecompose.core.expression.SymbolAnchor
import dev.sargunv.maplibrecompose.core.expression.SymbolPlacement
import dev.sargunv.maplibrecompose.core.expression.SymbolZOrder
import dev.sargunv.maplibrecompose.core.expression.TFormatted
import dev.sargunv.maplibrecompose.core.expression.TResolvedImage
import dev.sargunv.maplibrecompose.core.expression.TextJustify
import dev.sargunv.maplibrecompose.core.expression.TextPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.TextRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.TextTransform
import dev.sargunv.maplibrecompose.core.expression.TextWritingMode
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toNSExpression
import dev.sargunv.maplibrecompose.core.util.toNSPredicate

@PublishedApi
internal actual class SymbolLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {

  override val impl = MLNSymbolStyleLayer(id, source.impl)

  actual override var sourceLayer: String
    get() = impl.sourceLayerIdentifier!!
    set(value) {
      impl.sourceLayerIdentifier = value
    }

  actual override fun setFilter(filter: Expression<Boolean>) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setSymbolPlacement(placement: Expression<SymbolPlacement>) {
    impl.symbolPlacement = placement.toNSExpression()
  }

  actual fun setSymbolSpacing(spacing: Expression<Number>) {
    impl.symbolSpacing = spacing.toNSExpression()
  }

  actual fun setSymbolAvoidEdges(avoidEdges: Expression<Boolean>) {
    impl.symbolAvoidsEdges = avoidEdges.toNSExpression()
  }

  actual fun setSymbolSortKey(sortKey: Expression<Number>) {
    impl.symbolSortKey = sortKey.toNSExpression()
  }

  actual fun setSymbolZOrder(zOrder: Expression<SymbolZOrder>) {
    impl.symbolZOrder = zOrder.toNSExpression()
  }

  actual fun setIconAllowOverlap(allowOverlap: Expression<Boolean>) {
    impl.iconAllowsOverlap = allowOverlap.toNSExpression()
  }

  actual fun setIconOverlap(overlap: Expression<String>) {
    // not implemented by MapLibre-native iOS yet
    // impl.iconOverlap = overlap.toNSExpression()
  }

  actual fun setIconIgnorePlacement(ignorePlacement: Expression<Boolean>) {
    impl.iconIgnoresPlacement = ignorePlacement.toNSExpression()
  }

  actual fun setIconOptional(optional: Expression<Boolean>) {
    impl.iconOptional = optional.toNSExpression()
  }

  actual fun setIconRotationAlignment(rotationAlignment: Expression<IconRotationAlignment>) {
    impl.iconRotationAlignment = rotationAlignment.toNSExpression()
  }

  actual fun setIconSize(size: Expression<Number>) {
    impl.iconScale = size.toNSExpression()
  }

  actual fun setIconTextFit(textFit: Expression<IconTextFit>) {
    impl.iconTextFit = textFit.toNSExpression()
  }

  actual fun setIconTextFitPadding(textFitPadding: Expression<Insets>) {
    impl.iconTextFitPadding = textFitPadding.toNSExpression()
  }

  actual fun setIconImage(image: Expression<TResolvedImage>) {
    // TODO figure out how to unset an image
    if (image.value != null) impl.iconImageName = image.toNSExpression()
  }

  actual fun setIconRotate(rotate: Expression<Number>) {
    impl.iconRotation = rotate.toNSExpression()
  }

  actual fun setIconPadding(padding: Expression<Number>) {
    impl.iconPadding = padding.toNSExpression()
  }

  actual fun setIconKeepUpright(keepUpright: Expression<Boolean>) {
    impl.keepsIconUpright = keepUpright.toNSExpression()
  }

  actual fun setIconOffset(offset: Expression<Point>) {
    impl.iconOffset = offset.toNSExpression()
  }

  actual fun setIconAnchor(anchor: Expression<SymbolAnchor>) {
    impl.iconAnchor = anchor.toNSExpression()
  }

  actual fun setIconPitchAlignment(pitchAlignment: Expression<IconPitchAlignment>) {
    impl.iconPitchAlignment = pitchAlignment.toNSExpression()
  }

  actual fun setIconOpacity(opacity: Expression<Number>) {
    impl.iconOpacity = opacity.toNSExpression()
  }

  actual fun setIconColor(color: Expression<Color>) {
    impl.iconColor = color.toNSExpression()
  }

  actual fun setIconHaloColor(haloColor: Expression<Color>) {
    impl.iconHaloColor = haloColor.toNSExpression()
  }

  actual fun setIconHaloWidth(haloWidth: Expression<Number>) {
    impl.iconHaloWidth = haloWidth.toNSExpression()
  }

  actual fun setIconHaloBlur(haloBlur: Expression<Number>) {
    impl.iconHaloBlur = haloBlur.toNSExpression()
  }

  actual fun setIconTranslate(translate: Expression<Point>) {
    impl.iconTranslation = translate.toNSExpression()
  }

  actual fun setIconTranslateAnchor(translateAnchor: Expression<TranslateAnchor>) {
    impl.iconTranslationAnchor = translateAnchor.toNSExpression()
  }

  actual fun setTextPitchAlignment(pitchAlignment: Expression<TextPitchAlignment>) {
    impl.textPitchAlignment = pitchAlignment.toNSExpression()
  }

  actual fun setTextRotationAlignment(rotationAlignment: Expression<TextRotationAlignment>) {
    impl.textRotationAlignment = rotationAlignment.toNSExpression()
  }

  actual fun setTextField(field: Expression<TFormatted>) {
    impl.text = field.toNSExpression()
  }

  actual fun setTextFont(font: Expression<List<String>>) {
    impl.textFontNames = font.toNSExpression()
  }

  actual fun setTextSize(size: Expression<Number>) {
    impl.textFontSize = size.toNSExpression()
  }

  actual fun setTextMaxWidth(maxWidth: Expression<Number>) {
    impl.maximumTextWidth = maxWidth.toNSExpression()
  }

  actual fun setTextLineHeight(lineHeight: Expression<Number>) {
    impl.textLineHeight = lineHeight.toNSExpression()
  }

  actual fun setTextLetterSpacing(letterSpacing: Expression<Number>) {
    impl.textLetterSpacing = letterSpacing.toNSExpression()
  }

  actual fun setTextJustify(justify: Expression<TextJustify>) {
    impl.textJustification = justify.toNSExpression()
  }

  actual fun setTextRadialOffset(radialOffset: Expression<Number>) {
    impl.textRadialOffset = radialOffset.toNSExpression()
  }

  actual fun setTextVariableAnchor(variableAnchor: Expression<List<SymbolAnchor>>) {
    impl.textVariableAnchor = variableAnchor.toNSExpression()
  }

  actual fun setTextVariableAnchorOffset(
    variableAnchorOffset: Expression<List<Pair<SymbolAnchor, Point>>>
  ) {
    impl.textVariableAnchor = variableAnchorOffset.toNSExpression()
  }

  actual fun setTextAnchor(anchor: Expression<SymbolAnchor>) {
    impl.textAnchor = anchor.toNSExpression()
  }

  actual fun setTextMaxAngle(maxAngle: Expression<Number>) {
    impl.maximumTextAngle = maxAngle.toNSExpression()
  }

  actual fun setTextWritingMode(writingMode: Expression<List<TextWritingMode>>) {
    impl.textWritingModes = writingMode.toNSExpression()
  }

  actual fun setTextRotate(rotate: Expression<Number>) {
    impl.textRotation = rotate.toNSExpression()
  }

  actual fun setTextPadding(padding: Expression<Number>) {
    impl.textPadding = padding.toNSExpression()
  }

  actual fun setTextKeepUpright(keepUpright: Expression<Boolean>) {
    impl.keepsTextUpright = keepUpright.toNSExpression()
  }

  actual fun setTextTransform(transform: Expression<TextTransform>) {
    impl.textTransform = transform.toNSExpression()
  }

  actual fun setTextOffset(offset: Expression<Point>) {
    impl.textOffset = offset.toNSExpression()
  }

  actual fun setTextAllowOverlap(allowOverlap: Expression<Boolean>) {
    impl.textAllowsOverlap = allowOverlap.toNSExpression()
  }

  actual fun setTextOverlap(overlap: Expression<String>) {
    // not implemented by MapLibre-native iOS yet
    // impl.textOverlap = overlap.toNSExpression()
  }

  actual fun setTextIgnorePlacement(ignorePlacement: Expression<Boolean>) {
    impl.textIgnoresPlacement = ignorePlacement.toNSExpression()
  }

  actual fun setTextOptional(optional: Expression<Boolean>) {
    impl.textOptional = optional.toNSExpression()
  }

  actual fun setTextOpacity(opacity: Expression<Number>) {
    impl.textOpacity = opacity.toNSExpression()
  }

  actual fun setTextColor(color: Expression<Color>) {
    impl.textColor = color.toNSExpression()
  }

  actual fun setTextHaloColor(haloColor: Expression<Color>) {
    impl.textHaloColor = haloColor.toNSExpression()
  }

  actual fun setTextHaloWidth(haloWidth: Expression<Number>) {
    impl.textHaloWidth = haloWidth.toNSExpression()
  }

  actual fun setTextHaloBlur(haloBlur: Expression<Number>) {
    impl.textHaloBlur = haloBlur.toNSExpression()
  }

  actual fun setTextTranslate(translate: Expression<Point>) {
    impl.textTranslation = translate.toNSExpression()
  }

  actual fun setTextTranslateAnchor(translateAnchor: Expression<TranslateAnchor>) {
    impl.textTranslationAnchor = translateAnchor.toNSExpression()
  }
}
