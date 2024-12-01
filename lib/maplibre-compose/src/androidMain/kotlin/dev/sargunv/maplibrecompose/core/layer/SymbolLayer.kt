package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.Insets
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
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import org.maplibre.android.style.expressions.Expression as MLNExpression
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer as MLNSymbolLayer

@PublishedApi
internal actual class SymbolLayer actual constructor(id: String, source: Source) :
  FeatureLayer(source) {
  override val impl = MLNSymbolLayer(id, source.id)

  actual override var sourceLayer: String by impl::sourceLayer

  actual override fun setFilter(filter: Expression<Boolean>) {
    impl.setFilter(filter.toMLNExpression() ?: MLNExpression.literal(true))
  }

  actual fun setSymbolPlacement(placement: Expression<SymbolPlacement>) {
    impl.setProperties(PropertyFactory.symbolPlacement(placement.toMLNExpression()))
  }

  actual fun setSymbolSpacing(spacing: Expression<Number>) {
    impl.setProperties(PropertyFactory.symbolSpacing(spacing.toMLNExpression()))
  }

  actual fun setSymbolAvoidEdges(avoidEdges: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.symbolAvoidEdges(avoidEdges.toMLNExpression()))
  }

  actual fun setSymbolSortKey(sortKey: Expression<Number>) {
    impl.setProperties(PropertyFactory.symbolSortKey(sortKey.toMLNExpression()))
  }

  actual fun setSymbolZOrder(zOrder: Expression<SymbolZOrder>) {
    impl.setProperties(PropertyFactory.symbolZOrder(zOrder.toMLNExpression()))
  }

  actual fun setIconAllowOverlap(allowOverlap: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.iconAllowOverlap(allowOverlap.toMLNExpression()))
  }

  actual fun setIconOverlap(overlap: Expression<String>) {
    // not implemented by MapLibre-native Android yet
    // impl.setProperties(PropertyFactory.iconOverlap(overlap.toMLNExpression()))
  }

  actual fun setIconIgnorePlacement(ignorePlacement: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.iconIgnorePlacement(ignorePlacement.toMLNExpression()))
  }

  actual fun setIconOptional(optional: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.iconOptional(optional.toMLNExpression()))
  }

  actual fun setIconRotationAlignment(rotationAlignment: Expression<IconRotationAlignment>) {
    impl.setProperties(PropertyFactory.iconRotationAlignment(rotationAlignment.toMLNExpression()))
  }

  actual fun setIconSize(size: Expression<Number>) {
    impl.setProperties(PropertyFactory.iconSize(size.toMLNExpression()))
  }

  actual fun setIconTextFit(textFit: Expression<IconTextFit>) {
    impl.setProperties(PropertyFactory.iconTextFit(textFit.toMLNExpression()))
  }

  actual fun setIconTextFitPadding(textFitPadding: Expression<Insets>) {
    impl.setProperties(PropertyFactory.iconTextFitPadding(textFitPadding.toMLNExpression()))
  }

  actual fun setIconImage(image: Expression<TResolvedImage>) {
    impl.setProperties(PropertyFactory.iconImage(image.toMLNExpression()))
  }

  actual fun setIconRotate(rotate: Expression<Number>) {
    impl.setProperties(PropertyFactory.iconRotate(rotate.toMLNExpression()))
  }

  actual fun setIconPadding(padding: Expression<Number>) {
    impl.setProperties(PropertyFactory.iconPadding(padding.toMLNExpression()))
  }

  actual fun setIconKeepUpright(keepUpright: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.iconKeepUpright(keepUpright.toMLNExpression()))
  }

  actual fun setIconOffset(offset: Expression<Offset>) {
    impl.setProperties(PropertyFactory.iconOffset(offset.toMLNExpression()))
  }

  actual fun setIconAnchor(anchor: Expression<SymbolAnchor>) {
    impl.setProperties(PropertyFactory.iconAnchor(anchor.toMLNExpression()))
  }

  actual fun setIconPitchAlignment(pitchAlignment: Expression<IconPitchAlignment>) {
    impl.setProperties(PropertyFactory.iconPitchAlignment(pitchAlignment.toMLNExpression()))
  }

  actual fun setIconOpacity(opacity: Expression<Number>) {
    impl.setProperties(PropertyFactory.iconOpacity(opacity.toMLNExpression()))
  }

  actual fun setIconColor(color: Expression<Color>) {
    impl.setProperties(PropertyFactory.iconColor(color.toMLNExpression()))
  }

  actual fun setIconHaloColor(haloColor: Expression<Color>) {
    impl.setProperties(PropertyFactory.iconHaloColor(haloColor.toMLNExpression()))
  }

  actual fun setIconHaloWidth(haloWidth: Expression<Number>) {
    impl.setProperties(PropertyFactory.iconHaloWidth(haloWidth.toMLNExpression()))
  }

  actual fun setIconHaloBlur(haloBlur: Expression<Number>) {
    impl.setProperties(PropertyFactory.iconHaloBlur(haloBlur.toMLNExpression()))
  }

  actual fun setIconTranslate(translate: Expression<Offset>) {
    impl.setProperties(PropertyFactory.iconTranslate(translate.toMLNExpression()))
  }

  actual fun setIconTranslateAnchor(translateAnchor: Expression<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.iconTranslateAnchor(translateAnchor.toMLNExpression()))
  }

  actual fun setTextPitchAlignment(pitchAlignment: Expression<TextPitchAlignment>) {
    impl.setProperties(PropertyFactory.textPitchAlignment(pitchAlignment.toMLNExpression()))
  }

  actual fun setTextRotationAlignment(rotationAlignment: Expression<TextRotationAlignment>) {
    impl.setProperties(PropertyFactory.textRotationAlignment(rotationAlignment.toMLNExpression()))
  }

  actual fun setTextField(field: Expression<TFormatted>) {
    impl.setProperties(PropertyFactory.textField(field.toMLNExpression()))
  }

  actual fun setTextFont(font: Expression<List<String>>) {
    impl.setProperties(PropertyFactory.textFont(font.toMLNExpression()))
  }

  actual fun setTextSize(size: Expression<Number>) {
    impl.setProperties(PropertyFactory.textSize(size.toMLNExpression()))
  }

  actual fun setTextMaxWidth(maxWidth: Expression<Number>) {
    impl.setProperties(PropertyFactory.textMaxWidth(maxWidth.toMLNExpression()))
  }

  actual fun setTextLineHeight(lineHeight: Expression<Number>) {
    impl.setProperties(PropertyFactory.textLineHeight(lineHeight.toMLNExpression()))
  }

  actual fun setTextLetterSpacing(letterSpacing: Expression<Number>) {
    impl.setProperties(PropertyFactory.textLetterSpacing(letterSpacing.toMLNExpression()))
  }

  actual fun setTextJustify(justify: Expression<TextJustify>) {
    impl.setProperties(PropertyFactory.textJustify(justify.toMLNExpression()))
  }

  actual fun setTextRadialOffset(radialOffset: Expression<Number>) {
    impl.setProperties(PropertyFactory.textRadialOffset(radialOffset.toMLNExpression()))
  }

  actual fun setTextVariableAnchor(variableAnchor: Expression<List<SymbolAnchor>>) {
    impl.setProperties(PropertyFactory.textVariableAnchor(variableAnchor.toMLNExpression()))
  }

  actual fun setTextVariableAnchorOffset(
    variableAnchorOffset: Expression<List<Pair<SymbolAnchor, Offset>>>
  ) {
    impl.setProperties(
      PropertyFactory.textVariableAnchorOffset(variableAnchorOffset.toMLNExpression())
    )
  }

  actual fun setTextAnchor(anchor: Expression<SymbolAnchor>) {
    impl.setProperties(PropertyFactory.textAnchor(anchor.toMLNExpression()))
  }

  actual fun setTextMaxAngle(maxAngle: Expression<Number>) {
    impl.setProperties(PropertyFactory.textMaxAngle(maxAngle.toMLNExpression()))
  }

  actual fun setTextWritingMode(writingMode: Expression<List<TextWritingMode>>) {
    impl.setProperties(PropertyFactory.textWritingMode(writingMode.toMLNExpression()))
  }

  actual fun setTextRotate(rotate: Expression<Number>) {
    impl.setProperties(PropertyFactory.textRotate(rotate.toMLNExpression()))
  }

  actual fun setTextPadding(padding: Expression<Number>) {
    impl.setProperties(PropertyFactory.textPadding(padding.toMLNExpression()))
  }

  actual fun setTextKeepUpright(keepUpright: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.textKeepUpright(keepUpright.toMLNExpression()))
  }

  actual fun setTextTransform(transform: Expression<TextTransform>) {
    impl.setProperties(PropertyFactory.textTransform(transform.toMLNExpression()))
  }

  actual fun setTextOffset(offset: Expression<Offset>) {
    impl.setProperties(PropertyFactory.textOffset(offset.toMLNExpression()))
  }

  actual fun setTextAllowOverlap(allowOverlap: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.textAllowOverlap(allowOverlap.toMLNExpression()))
  }

  actual fun setTextOverlap(overlap: Expression<String>) {
    // not implemented by MapLibre-native Android yet
    // impl.setProperties(PropertyFactory.textOverlap(overlap.toMLNExpression()))
  }

  actual fun setTextIgnorePlacement(ignorePlacement: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.textIgnorePlacement(ignorePlacement.toMLNExpression()))
  }

  actual fun setTextOptional(optional: Expression<Boolean>) {
    impl.setProperties(PropertyFactory.textOptional(optional.toMLNExpression()))
  }

  actual fun setTextOpacity(opacity: Expression<Number>) {
    impl.setProperties(PropertyFactory.textOpacity(opacity.toMLNExpression()))
  }

  actual fun setTextColor(color: Expression<Color>) {
    impl.setProperties(PropertyFactory.textColor(color.toMLNExpression()))
  }

  actual fun setTextHaloColor(haloColor: Expression<Color>) {
    impl.setProperties(PropertyFactory.textHaloColor(haloColor.toMLNExpression()))
  }

  actual fun setTextHaloWidth(haloWidth: Expression<Number>) {
    impl.setProperties(PropertyFactory.textHaloWidth(haloWidth.toMLNExpression()))
  }

  actual fun setTextHaloBlur(haloBlur: Expression<Number>) {
    impl.setProperties(PropertyFactory.textHaloBlur(haloBlur.toMLNExpression()))
  }

  actual fun setTextTranslate(translate: Expression<Offset>) {
    impl.setProperties(PropertyFactory.textTranslate(translate.toMLNExpression()))
  }

  actual fun setTextTranslateAnchor(translateAnchor: Expression<TranslateAnchor>) {
    impl.setProperties(PropertyFactory.textTranslateAnchor(translateAnchor.toMLNExpression()))
  }
}
