package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanExpression
import dev.sargunv.maplibrecompose.core.expression.ColorExpression
import dev.sargunv.maplibrecompose.core.expression.DpExpression
import dev.sargunv.maplibrecompose.core.expression.DpOffsetExpression
import dev.sargunv.maplibrecompose.core.expression.EnumExpression
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.FloatExpression
import dev.sargunv.maplibrecompose.core.expression.FormattedExpression
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.ImageExpression
import dev.sargunv.maplibrecompose.core.expression.ListExpression
import dev.sargunv.maplibrecompose.core.expression.OffsetExpression
import dev.sargunv.maplibrecompose.core.expression.PaddingExpression
import dev.sargunv.maplibrecompose.core.expression.StringExpression
import dev.sargunv.maplibrecompose.core.expression.StringValue
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
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import org.maplibre.android.style.expressions.Expression as MLNExpression
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer as MLNSymbolLayer

@PublishedApi
internal actual class SymbolLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {
  override val impl = MLNSymbolLayer(id, source.id)

  actual override var sourceLayer: String by impl::sourceLayer

  actual override fun setFilter(filter: BooleanExpression) {
    impl.setFilter(filter.toMLNExpression() ?: MLNExpression.literal(true))
  }

  actual fun setSymbolPlacement(placement: EnumExpression<SymbolPlacement>) {
    impl.setProperties(PropertyFactory.symbolPlacement(placement.toMLNExpression()))
  }

  actual fun setSymbolSpacing(spacing: DpExpression) {
    impl.setProperties(PropertyFactory.symbolSpacing(spacing.toMLNExpression()))
  }

  actual fun setSymbolAvoidEdges(avoidEdges: BooleanExpression) {
    impl.setProperties(PropertyFactory.symbolAvoidEdges(avoidEdges.toMLNExpression()))
  }

  actual fun setSymbolSortKey(sortKey: FloatExpression) {
    impl.setProperties(PropertyFactory.symbolSortKey(sortKey.toMLNExpression()))
  }

  actual fun setSymbolZOrder(zOrder: EnumExpression<SymbolZOrder>) {
    impl.setProperties(PropertyFactory.symbolZOrder(zOrder.toMLNExpression()))
  }

  actual fun setIconAllowOverlap(allowOverlap: BooleanExpression) {
    impl.setProperties(PropertyFactory.iconAllowOverlap(allowOverlap.toMLNExpression()))
  }

  actual fun setIconOverlap(overlap: StringExpression) {
    // not implemented by MapLibre-native Android yet
    // impl.setProperties(PropertyFactory.iconOverlap(overlap.toMLNExpression()))
  }

  actual fun setIconIgnorePlacement(ignorePlacement: BooleanExpression) {
    impl.setProperties(PropertyFactory.iconIgnorePlacement(ignorePlacement.toMLNExpression()))
  }

  actual fun setIconOptional(optional: BooleanExpression) {
    impl.setProperties(PropertyFactory.iconOptional(optional.toMLNExpression()))
  }

  actual fun setIconRotationAlignment(rotationAlignment: EnumExpression<IconRotationAlignment>) {
    impl.setProperties(PropertyFactory.iconRotationAlignment(rotationAlignment.toMLNExpression()))
  }

  actual fun setIconSize(size: FloatExpression) {
    impl.setProperties(PropertyFactory.iconSize(size.toMLNExpression()))
  }

  actual fun setIconTextFit(textFit: EnumExpression<IconTextFit>) {
    impl.setProperties(PropertyFactory.iconTextFit(textFit.toMLNExpression()))
  }

  actual fun setIconTextFitPadding(textFitPadding: PaddingExpression) {
    impl.setProperties(PropertyFactory.iconTextFitPadding(textFitPadding.toMLNExpression()))
  }

  actual fun setIconImage(image: ImageExpression) {
    impl.setProperties(PropertyFactory.iconImage(image.toMLNExpression()))
  }

  actual fun setIconRotate(rotate: FloatExpression) {
    impl.setProperties(PropertyFactory.iconRotate(rotate.toMLNExpression()))
  }

  actual fun setIconPadding(padding: DpExpression) {
    impl.setProperties(PropertyFactory.iconPadding(padding.toMLNExpression()))
  }

  actual fun setIconKeepUpright(keepUpright: BooleanExpression) {
    impl.setProperties(PropertyFactory.iconKeepUpright(keepUpright.toMLNExpression()))
  }

  actual fun setIconOffset(offset: DpOffsetExpression) {
    impl.setProperties(PropertyFactory.iconOffset(offset.toMLNExpression()))
  }

  actual fun setIconAnchor(anchor: EnumExpression<SymbolAnchor>) {
    impl.setProperties(PropertyFactory.iconAnchor(anchor.toMLNExpression()))
  }

  actual fun setIconPitchAlignment(pitchAlignment: EnumExpression<IconPitchAlignment>) {
    impl.setProperties(PropertyFactory.iconPitchAlignment(pitchAlignment.toMLNExpression()))
  }

  actual fun setIconOpacity(opacity: FloatExpression) {
    impl.setProperties(PropertyFactory.iconOpacity(opacity.toMLNExpression()))
  }

  actual fun setIconColor(color: ColorExpression) {
    impl.setProperties(PropertyFactory.iconColor(color.toMLNExpression()))
  }

  actual fun setIconHaloColor(haloColor: ColorExpression) {
    impl.setProperties(PropertyFactory.iconHaloColor(haloColor.toMLNExpression()))
  }

  actual fun setIconHaloWidth(haloWidth: DpExpression) {
    impl.setProperties(PropertyFactory.iconHaloWidth(haloWidth.toMLNExpression()))
  }

  actual fun setIconHaloBlur(haloBlur: DpExpression) {
    impl.setProperties(PropertyFactory.iconHaloBlur(haloBlur.toMLNExpression()))
  }

  actual fun setIconTranslate(translate: DpOffsetExpression) {
    impl.setProperties(PropertyFactory.iconTranslate(translate.toMLNExpression()))
  }

  actual fun setIconTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.iconTranslateAnchor(translateAnchor.toMLNExpression()))
  }

  actual fun setTextPitchAlignment(pitchAlignment: EnumExpression<TextPitchAlignment>) {
    impl.setProperties(PropertyFactory.textPitchAlignment(pitchAlignment.toMLNExpression()))
  }

  actual fun setTextRotationAlignment(rotationAlignment: EnumExpression<TextRotationAlignment>) {
    impl.setProperties(PropertyFactory.textRotationAlignment(rotationAlignment.toMLNExpression()))
  }

  actual fun setTextField(field: FormattedExpression) {
    impl.setProperties(PropertyFactory.textField(field.toMLNExpression()))
  }

  actual fun setTextFont(font: ListExpression<StringValue>) {
    impl.setProperties(PropertyFactory.textFont(font.toMLNExpression()))
  }

  actual fun setTextSize(size: DpExpression) {
    impl.setProperties(PropertyFactory.textSize(size.toMLNExpression()))
  }

  actual fun setTextMaxWidth(maxWidth: FloatExpression) {
    impl.setProperties(PropertyFactory.textMaxWidth(maxWidth.toMLNExpression()))
  }

  actual fun setTextLineHeight(lineHeight: FloatExpression) {
    impl.setProperties(PropertyFactory.textLineHeight(lineHeight.toMLNExpression()))
  }

  actual fun setTextLetterSpacing(letterSpacing: FloatExpression) {
    impl.setProperties(PropertyFactory.textLetterSpacing(letterSpacing.toMLNExpression()))
  }

  actual fun setTextJustify(justify: EnumExpression<TextJustify>) {
    impl.setProperties(PropertyFactory.textJustify(justify.toMLNExpression()))
  }

  actual fun setTextRadialOffset(radialOffset: FloatExpression) {
    impl.setProperties(PropertyFactory.textRadialOffset(radialOffset.toMLNExpression()))
  }

  actual fun setTextVariableAnchor(variableAnchor: ListExpression<EnumValue<SymbolAnchor>>) {
    impl.setProperties(PropertyFactory.textVariableAnchor(variableAnchor.toMLNExpression()))
  }

  actual fun setTextVariableAnchorOffset(variableAnchorOffset: ListExpression<*>) {
    impl.setProperties(
      PropertyFactory.textVariableAnchorOffset(variableAnchorOffset.toMLNExpression())
    )
  }

  actual fun setTextAnchor(anchor: EnumExpression<SymbolAnchor>) {
    impl.setProperties(PropertyFactory.textAnchor(anchor.toMLNExpression()))
  }

  actual fun setTextMaxAngle(maxAngle: FloatExpression) {
    impl.setProperties(PropertyFactory.textMaxAngle(maxAngle.toMLNExpression()))
  }

  actual fun setTextWritingMode(writingMode: ListExpression<EnumValue<TextWritingMode>>) {
    impl.setProperties(PropertyFactory.textWritingMode(writingMode.toMLNExpression()))
  }

  actual fun setTextRotate(rotate: FloatExpression) {
    impl.setProperties(PropertyFactory.textRotate(rotate.toMLNExpression()))
  }

  actual fun setTextPadding(padding: DpExpression) {
    impl.setProperties(PropertyFactory.textPadding(padding.toMLNExpression()))
  }

  actual fun setTextKeepUpright(keepUpright: BooleanExpression) {
    impl.setProperties(PropertyFactory.textKeepUpright(keepUpright.toMLNExpression()))
  }

  actual fun setTextTransform(transform: EnumExpression<TextTransform>) {
    impl.setProperties(PropertyFactory.textTransform(transform.toMLNExpression()))
  }

  actual fun setTextOffset(offset: OffsetExpression) {
    impl.setProperties(PropertyFactory.textOffset(offset.toMLNExpression()))
  }

  actual fun setTextAllowOverlap(allowOverlap: BooleanExpression) {
    impl.setProperties(PropertyFactory.textAllowOverlap(allowOverlap.toMLNExpression()))
  }

  actual fun setTextOverlap(overlap: StringExpression) {
    // not implemented by MapLibre-native Android yet
    // impl.setProperties(PropertyFactory.textOverlap(overlap.toMLNExpression()))
  }

  actual fun setTextIgnorePlacement(ignorePlacement: BooleanExpression) {
    impl.setProperties(PropertyFactory.textIgnorePlacement(ignorePlacement.toMLNExpression()))
  }

  actual fun setTextOptional(optional: BooleanExpression) {
    impl.setProperties(PropertyFactory.textOptional(optional.toMLNExpression()))
  }

  actual fun setTextOpacity(opacity: FloatExpression) {
    impl.setProperties(PropertyFactory.textOpacity(opacity.toMLNExpression()))
  }

  actual fun setTextColor(color: ColorExpression) {
    impl.setProperties(PropertyFactory.textColor(color.toMLNExpression()))
  }

  actual fun setTextHaloColor(haloColor: ColorExpression) {
    impl.setProperties(PropertyFactory.textHaloColor(haloColor.toMLNExpression()))
  }

  actual fun setTextHaloWidth(haloWidth: DpExpression) {
    impl.setProperties(PropertyFactory.textHaloWidth(haloWidth.toMLNExpression()))
  }

  actual fun setTextHaloBlur(haloBlur: DpExpression) {
    impl.setProperties(PropertyFactory.textHaloBlur(haloBlur.toMLNExpression()))
  }

  actual fun setTextTranslate(translate: DpOffsetExpression) {
    impl.setProperties(PropertyFactory.textTranslate(translate.toMLNExpression()))
  }

  actual fun setTextTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.textTranslateAnchor(translateAnchor.toMLNExpression()))
  }
}
