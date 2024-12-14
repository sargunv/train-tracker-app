package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.FormattedValue
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.ImageValue
import dev.sargunv.maplibrecompose.core.expression.ListValue
import dev.sargunv.maplibrecompose.core.expression.OffsetValue
import dev.sargunv.maplibrecompose.core.expression.PaddingValue
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

@PublishedApi
internal expect class SymbolLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<BooleanValue>)

  fun setSymbolPlacement(placement: Expression<EnumValue<SymbolPlacement>>)

  fun setSymbolSpacing(spacing: Expression<DpValue>)

  fun setSymbolAvoidEdges(avoidEdges: Expression<BooleanValue>)

  fun setSymbolSortKey(sortKey: Expression<FloatValue>)

  fun setSymbolZOrder(zOrder: Expression<EnumValue<SymbolZOrder>>)

  fun setIconAllowOverlap(allowOverlap: Expression<BooleanValue>)

  fun setIconOverlap(overlap: Expression<StringValue>)

  fun setIconIgnorePlacement(ignorePlacement: Expression<BooleanValue>)

  fun setIconOptional(optional: Expression<BooleanValue>)

  fun setIconRotationAlignment(rotationAlignment: Expression<EnumValue<IconRotationAlignment>>)

  fun setIconSize(size: Expression<FloatValue>)

  fun setIconTextFit(textFit: Expression<EnumValue<IconTextFit>>)

  fun setIconTextFitPadding(textFitPadding: Expression<PaddingValue>)

  fun setIconImage(image: Expression<ImageValue>)

  fun setIconRotate(rotate: Expression<FloatValue>)

  fun setIconPadding(padding: Expression<DpValue>)

  fun setIconKeepUpright(keepUpright: Expression<BooleanValue>)

  fun setIconOffset(offset: Expression<DpOffsetValue>)

  fun setIconAnchor(anchor: Expression<EnumValue<SymbolAnchor>>)

  fun setIconPitchAlignment(pitchAlignment: Expression<EnumValue<IconPitchAlignment>>)

  fun setIconOpacity(opacity: Expression<FloatValue>)

  fun setIconColor(color: Expression<ColorValue>)

  fun setIconHaloColor(haloColor: Expression<ColorValue>)

  fun setIconHaloWidth(haloWidth: Expression<DpValue>)

  fun setIconHaloBlur(haloBlur: Expression<DpValue>)

  fun setIconTranslate(translate: Expression<DpOffsetValue>)

  fun setIconTranslateAnchor(translateAnchor: Expression<EnumValue<TranslateAnchor>>)

  fun setTextPitchAlignment(pitchAlignment: Expression<EnumValue<TextPitchAlignment>>)

  fun setTextRotationAlignment(rotationAlignment: Expression<EnumValue<TextRotationAlignment>>)

  fun setTextField(field: Expression<FormattedValue>)

  fun setTextFont(font: Expression<ListValue<StringValue>>)

  fun setTextSize(size: Expression<DpValue>)

  fun setTextMaxWidth(maxWidth: Expression<FloatValue>)

  fun setTextLineHeight(lineHeight: Expression<FloatValue>)

  fun setTextLetterSpacing(letterSpacing: Expression<FloatValue>)

  fun setTextJustify(justify: Expression<EnumValue<TextJustify>>)

  fun setTextRadialOffset(radialOffset: Expression<FloatValue>)

  fun setTextVariableAnchor(variableAnchor: Expression<ListValue<EnumValue<SymbolAnchor>>>)

  // TODO: this is a list of alternating SymbolAnchor and Offset
  fun setTextVariableAnchorOffset(variableAnchorOffset: Expression<ListValue<*>>)

  fun setTextAnchor(anchor: Expression<EnumValue<SymbolAnchor>>)

  fun setTextMaxAngle(maxAngle: Expression<FloatValue>)

  fun setTextWritingMode(writingMode: Expression<ListValue<EnumValue<TextWritingMode>>>)

  fun setTextRotate(rotate: Expression<FloatValue>)

  fun setTextPadding(padding: Expression<DpValue>)

  fun setTextKeepUpright(keepUpright: Expression<BooleanValue>)

  fun setTextTransform(transform: Expression<EnumValue<TextTransform>>)

  fun setTextOffset(offset: Expression<OffsetValue>)

  fun setTextAllowOverlap(allowOverlap: Expression<BooleanValue>)

  fun setTextOverlap(overlap: Expression<StringValue>)

  fun setTextIgnorePlacement(ignorePlacement: Expression<BooleanValue>)

  fun setTextOptional(optional: Expression<BooleanValue>)

  fun setTextOpacity(opacity: Expression<FloatValue>)

  fun setTextColor(color: Expression<ColorValue>)

  fun setTextHaloColor(haloColor: Expression<ColorValue>)

  fun setTextHaloWidth(haloWidth: Expression<DpValue>)

  fun setTextHaloBlur(haloBlur: Expression<DpValue>)

  fun setTextTranslate(translate: Expression<DpOffsetValue>)

  fun setTextTranslateAnchor(translateAnchor: Expression<EnumValue<TranslateAnchor>>)
}
