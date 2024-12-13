package dev.sargunv.maplibrecompose.core.layer

import cocoapods.MapLibre.MLNSymbolStyleLayer
import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.FormattedExpression
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.ImageExpression
import dev.sargunv.maplibrecompose.core.expression.IntExpression
import dev.sargunv.maplibrecompose.core.expression.ListExpression
import dev.sargunv.maplibrecompose.core.expression.OffsetExpression
import dev.sargunv.maplibrecompose.core.expression.PaddingExpression
import dev.sargunv.maplibrecompose.core.expression.StringExpression
import dev.sargunv.maplibrecompose.core.expression.SymbolAnchor
import dev.sargunv.maplibrecompose.core.expression.SymbolPlacement
import dev.sargunv.maplibrecompose.core.expression.SymbolZOrder
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

  actual override fun setFilter(filter: BooleanExpression) {
    impl.predicate = filter.toNSPredicate()
  }

  actual fun setSymbolPlacement(placement: EnumExpression<SymbolPlacement>) {
    impl.symbolPlacement = placement.toNSExpression()
  }

  actual fun setSymbolSpacing(spacing: DpExpression) {
    impl.symbolSpacing = spacing.toNSExpression()
  }

  actual fun setSymbolAvoidEdges(avoidEdges: BooleanExpression) {
    impl.symbolAvoidsEdges = avoidEdges.toNSExpression()
  }

  actual fun setSymbolSortKey(sortKey: FloatExpression) {
    impl.symbolSortKey = sortKey.toNSExpression()
  }

  actual fun setSymbolZOrder(zOrder: EnumExpression<SymbolZOrder>) {
    impl.symbolZOrder = zOrder.toNSExpression()
  }

  actual fun setIconAllowOverlap(allowOverlap: BooleanExpression) {
    impl.iconAllowsOverlap = allowOverlap.toNSExpression()
  }

  actual fun setIconOverlap(overlap: StringExpression) {
    // not implemented by MapLibre-native iOS yet
    // impl.iconOverlap = overlap.toNSExpression()
  }

  actual fun setIconIgnorePlacement(ignorePlacement: BooleanExpression) {
    impl.iconIgnoresPlacement = ignorePlacement.toNSExpression()
  }

  actual fun setIconOptional(optional: BooleanExpression) {
    impl.iconOptional = optional.toNSExpression()
  }

  actual fun setIconRotationAlignment(rotationAlignment: EnumExpression<IconRotationAlignment>) {
    impl.iconRotationAlignment = rotationAlignment.toNSExpression()
  }

  actual fun setIconSize(size: FloatExpression) {
    impl.iconScale = size.toNSExpression()
  }

  actual fun setIconTextFit(textFit: EnumExpression<IconTextFit>) {
    impl.iconTextFit = textFit.toNSExpression()
  }

  actual fun setIconTextFitPadding(textFitPadding: PaddingExpression) {
    impl.iconTextFitPadding = textFitPadding.toNSExpression()
  }

  actual fun setIconImage(image: ImageExpression) {
    // TODO figure out how to unset an image
    if (image.value != null) impl.iconImageName = image.toNSExpression()
  }

  actual fun setIconRotate(rotate: IntExpression) {
    impl.iconRotation = rotate.toNSExpression()
  }

  actual fun setIconPadding(padding: DpExpression) {
    impl.iconPadding = padding.toNSExpression()
  }

  actual fun setIconKeepUpright(keepUpright: BooleanExpression) {
    impl.keepsIconUpright = keepUpright.toNSExpression()
  }

  actual fun setIconOffset(offset: DpOffsetExpression) {
    impl.iconOffset = offset.toNSExpression()
  }

  actual fun setIconAnchor(anchor: EnumExpression<SymbolAnchor>) {
    impl.iconAnchor = anchor.toNSExpression()
  }

  actual fun setIconPitchAlignment(pitchAlignment: EnumExpression<IconPitchAlignment>) {
    impl.iconPitchAlignment = pitchAlignment.toNSExpression()
  }

  actual fun setIconOpacity(opacity: FloatExpression) {
    impl.iconOpacity = opacity.toNSExpression()
  }

  actual fun setIconColor(color: ColorExpression) {
    impl.iconColor = color.toNSExpression()
  }

  actual fun setIconHaloColor(haloColor: ColorExpression) {
    impl.iconHaloColor = haloColor.toNSExpression()
  }

  actual fun setIconHaloWidth(haloWidth: DpExpression) {
    impl.iconHaloWidth = haloWidth.toNSExpression()
  }

  actual fun setIconHaloBlur(haloBlur: DpExpression) {
    impl.iconHaloBlur = haloBlur.toNSExpression()
  }

  actual fun setIconTranslate(translate: DpOffsetExpression) {
    impl.iconTranslation = translate.toNSExpression()
  }

  actual fun setIconTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>) {
    impl.iconTranslationAnchor = translateAnchor.toNSExpression()
  }

  actual fun setTextPitchAlignment(pitchAlignment: EnumExpression<TextPitchAlignment>) {
    impl.textPitchAlignment = pitchAlignment.toNSExpression()
  }

  actual fun setTextRotationAlignment(rotationAlignment: EnumExpression<TextRotationAlignment>) {
    impl.textRotationAlignment = rotationAlignment.toNSExpression()
  }

  actual fun setTextField(field: FormattedExpression) {
    impl.text = field.toNSExpression()
  }

  actual fun setTextFont(font: ListExpression<StringExpression>) {
    impl.textFontNames = font.toNSExpression()
  }

  actual fun setTextSize(size: DpExpression) {
    impl.textFontSize = size.toNSExpression()
  }

  actual fun setTextMaxWidth(maxWidth: FloatExpression) {
    impl.maximumTextWidth = maxWidth.toNSExpression()
  }

  actual fun setTextLineHeight(lineHeight: FloatExpression) {
    impl.textLineHeight = lineHeight.toNSExpression()
  }

  actual fun setTextLetterSpacing(letterSpacing: FloatExpression) {
    impl.textLetterSpacing = letterSpacing.toNSExpression()
  }

  actual fun setTextJustify(justify: EnumExpression<TextJustify>) {
    impl.textJustification = justify.toNSExpression()
  }

  actual fun setTextRadialOffset(radialOffset: FloatExpression) {
    impl.textRadialOffset = radialOffset.toNSExpression()
  }

  actual fun setTextVariableAnchor(variableAnchor: ListExpression<EnumExpression<SymbolAnchor>>) {
    impl.textVariableAnchor = variableAnchor.toNSExpression()
  }

  actual fun setTextVariableAnchorOffset(variableAnchorOffset: ListExpression<*>) {
    impl.textVariableAnchor = variableAnchorOffset.toNSExpression()
  }

  actual fun setTextAnchor(anchor: EnumExpression<SymbolAnchor>) {
    impl.textAnchor = anchor.toNSExpression()
  }

  actual fun setTextMaxAngle(maxAngle: IntExpression) {
    impl.maximumTextAngle = maxAngle.toNSExpression()
  }

  actual fun setTextWritingMode(writingMode: ListExpression<EnumExpression<TextWritingMode>>) {
    impl.textWritingModes = writingMode.toNSExpression()
  }

  actual fun setTextRotate(rotate: IntExpression) {
    impl.textRotation = rotate.toNSExpression()
  }

  actual fun setTextPadding(padding: DpExpression) {
    impl.textPadding = padding.toNSExpression()
  }

  actual fun setTextKeepUpright(keepUpright: BooleanExpression) {
    impl.keepsTextUpright = keepUpright.toNSExpression()
  }

  actual fun setTextTransform(transform: EnumExpression<TextTransform>) {
    impl.textTransform = transform.toNSExpression()
  }

  actual fun setTextOffset(offset: OffsetExpression) {
    impl.textOffset = offset.toNSExpression()
  }

  actual fun setTextAllowOverlap(allowOverlap: BooleanExpression) {
    impl.textAllowsOverlap = allowOverlap.toNSExpression()
  }

  actual fun setTextOverlap(overlap: StringExpression) {
    // not implemented by MapLibre-native iOS yet
    // impl.textOverlap = overlap.toNSExpression()
  }

  actual fun setTextIgnorePlacement(ignorePlacement: BooleanExpression) {
    impl.textIgnoresPlacement = ignorePlacement.toNSExpression()
  }

  actual fun setTextOptional(optional: BooleanExpression) {
    impl.textOptional = optional.toNSExpression()
  }

  actual fun setTextOpacity(opacity: FloatExpression) {
    impl.textOpacity = opacity.toNSExpression()
  }

  actual fun setTextColor(color: ColorExpression) {
    impl.textColor = color.toNSExpression()
  }

  actual fun setTextHaloColor(haloColor: ColorExpression) {
    impl.textHaloColor = haloColor.toNSExpression()
  }

  actual fun setTextHaloWidth(haloWidth: DpExpression) {
    impl.textHaloWidth = haloWidth.toNSExpression()
  }

  actual fun setTextHaloBlur(haloBlur: DpExpression) {
    impl.textHaloBlur = haloBlur.toNSExpression()
  }

  actual fun setTextTranslate(translate: DpOffsetExpression) {
    impl.textTranslation = translate.toNSExpression()
  }

  actual fun setTextTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>) {
    impl.textTranslationAnchor = translateAnchor.toNSExpression()
  }
}
