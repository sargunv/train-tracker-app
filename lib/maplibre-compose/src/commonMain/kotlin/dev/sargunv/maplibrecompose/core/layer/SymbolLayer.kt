package dev.sargunv.maplibrecompose.core.layer

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

@PublishedApi
internal expect class SymbolLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: BooleanExpression)

  fun setSymbolPlacement(placement: EnumExpression<SymbolPlacement>)

  fun setSymbolSpacing(spacing: DpExpression)

  fun setSymbolAvoidEdges(avoidEdges: BooleanExpression)

  fun setSymbolSortKey(sortKey: FloatExpression)

  fun setSymbolZOrder(zOrder: EnumExpression<SymbolZOrder>)

  fun setIconAllowOverlap(allowOverlap: BooleanExpression)

  fun setIconOverlap(overlap: StringExpression)

  fun setIconIgnorePlacement(ignorePlacement: BooleanExpression)

  fun setIconOptional(optional: BooleanExpression)

  fun setIconRotationAlignment(rotationAlignment: EnumExpression<IconRotationAlignment>)

  fun setIconSize(size: FloatExpression)

  fun setIconTextFit(textFit: EnumExpression<IconTextFit>)

  fun setIconTextFitPadding(textFitPadding: PaddingExpression)

  fun setIconImage(image: ImageExpression)

  fun setIconRotate(rotate: IntExpression)

  fun setIconPadding(padding: DpExpression)

  fun setIconKeepUpright(keepUpright: BooleanExpression)

  fun setIconOffset(offset: DpOffsetExpression)

  fun setIconAnchor(anchor: EnumExpression<SymbolAnchor>)

  fun setIconPitchAlignment(pitchAlignment: EnumExpression<IconPitchAlignment>)

  fun setIconOpacity(opacity: FloatExpression)

  fun setIconColor(color: ColorExpression)

  fun setIconHaloColor(haloColor: ColorExpression)

  fun setIconHaloWidth(haloWidth: DpExpression)

  fun setIconHaloBlur(haloBlur: DpExpression)

  fun setIconTranslate(translate: DpOffsetExpression)

  fun setIconTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>)

  fun setTextPitchAlignment(pitchAlignment: EnumExpression<TextPitchAlignment>)

  fun setTextRotationAlignment(rotationAlignment: EnumExpression<TextRotationAlignment>)

  fun setTextField(field: FormattedExpression)

  fun setTextFont(font: ListExpression<StringExpression>)

  fun setTextSize(size: DpExpression)

  fun setTextMaxWidth(maxWidth: FloatExpression)

  fun setTextLineHeight(lineHeight: FloatExpression)

  fun setTextLetterSpacing(letterSpacing: FloatExpression)

  fun setTextJustify(justify: EnumExpression<TextJustify>)

  fun setTextRadialOffset(radialOffset: FloatExpression)

  fun setTextVariableAnchor(variableAnchor: ListExpression<EnumExpression<SymbolAnchor>>)

  // TODO: this is a list of alternating SymbolAnchor and Offset
  fun setTextVariableAnchorOffset(variableAnchorOffset: ListExpression<*>)

  fun setTextAnchor(anchor: EnumExpression<SymbolAnchor>)

  fun setTextMaxAngle(maxAngle: IntExpression)

  fun setTextWritingMode(writingMode: ListExpression<EnumExpression<TextWritingMode>>)

  fun setTextRotate(rotate: IntExpression)

  fun setTextPadding(padding: DpExpression)

  fun setTextKeepUpright(keepUpright: BooleanExpression)

  fun setTextTransform(transform: EnumExpression<TextTransform>)

  fun setTextOffset(offset: OffsetExpression)

  fun setTextAllowOverlap(allowOverlap: BooleanExpression)

  fun setTextOverlap(overlap: StringExpression)

  fun setTextIgnorePlacement(ignorePlacement: BooleanExpression)

  fun setTextOptional(optional: BooleanExpression)

  fun setTextOpacity(opacity: FloatExpression)

  fun setTextColor(color: ColorExpression)

  fun setTextHaloColor(haloColor: ColorExpression)

  fun setTextHaloWidth(haloWidth: DpExpression)

  fun setTextHaloBlur(haloBlur: DpExpression)

  fun setTextTranslate(translate: DpOffsetExpression)

  fun setTextTranslateAnchor(translateAnchor: EnumExpression<TranslateAnchor>)
}
