package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.SymbolAnchor
import dev.sargunv.maplibrecompose.core.expression.SymbolPlacement
import dev.sargunv.maplibrecompose.core.expression.SymbolZOrder
import dev.sargunv.maplibrecompose.core.expression.TextJustify
import dev.sargunv.maplibrecompose.core.expression.TextPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.TextRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.TextTransform
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect class SymbolLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression.Boolean)

  fun setSymbolPlacement(placement: Expression.Enum<SymbolPlacement>)

  fun setSymbolSpacing(spacing: Expression.Dp)

  fun setSymbolAvoidEdges(avoidEdges: Expression.Boolean)

  fun setSymbolSortKey(sortKey: Expression.Number)

  fun setSymbolZOrder(zOrder: Expression.Enum<SymbolZOrder>)

  fun setIconAllowOverlap(allowOverlap: Expression.Boolean)

  fun setIconOverlap(overlap: Expression.String)

  fun setIconIgnorePlacement(ignorePlacement: Expression.Boolean)

  fun setIconOptional(optional: Expression.Boolean)

  fun setIconRotationAlignment(rotationAlignment: Expression.Enum<IconRotationAlignment>)

  fun setIconSize(size: Expression.Number)

  fun setIconTextFit(textFit: Expression.Enum<IconTextFit>)

  fun setIconTextFitPadding(textFitPadding: Expression.Padding)

  fun setIconImage(image: Expression.ResolvedImage)

  fun setIconRotate(rotate: Expression.Number)

  fun setIconPadding(padding: Expression.Dp)

  fun setIconKeepUpright(keepUpright: Expression.Boolean)

  fun setIconOffset(offset: Expression.DpOffset)

  fun setIconAnchor(anchor: Expression.Enum<SymbolAnchor>)

  fun setIconPitchAlignment(pitchAlignment: Expression.Enum<IconPitchAlignment>)

  fun setIconOpacity(opacity: Expression.Number)

  fun setIconColor(color: Expression.Color)

  fun setIconHaloColor(haloColor: Expression.Color)

  fun setIconHaloWidth(haloWidth: Expression.Dp)

  fun setIconHaloBlur(haloBlur: Expression.Dp)

  fun setIconTranslate(translate: Expression.DpOffset)

  fun setIconTranslateAnchor(translateAnchor: Expression.Enum<TranslateAnchor>)

  fun setTextPitchAlignment(pitchAlignment: Expression.Enum<TextPitchAlignment>)

  fun setTextRotationAlignment(rotationAlignment: Expression.Enum<TextRotationAlignment>)

  fun setTextField(field: Expression.Formatted)

  fun setTextFont(font: Expression.List)

  fun setTextSize(size: Expression.Dp)

  fun setTextMaxWidth(maxWidth: Expression.Number)

  fun setTextLineHeight(lineHeight: Expression.Number)

  fun setTextLetterSpacing(letterSpacing: Expression.Number)

  fun setTextJustify(justify: Expression.Enum<TextJustify>)

  fun setTextRadialOffset(radialOffset: Expression.Number)

  fun setTextVariableAnchor(variableAnchor: Expression.List)

  fun setTextVariableAnchorOffset(variableAnchorOffset: Expression.List)

  fun setTextAnchor(anchor: Expression.Enum<SymbolAnchor>)

  fun setTextMaxAngle(maxAngle: Expression.Number)

  fun setTextWritingMode(writingMode: Expression.List)

  fun setTextRotate(rotate: Expression.Number)

  fun setTextPadding(padding: Expression.Dp)

  fun setTextKeepUpright(keepUpright: Expression.Boolean)

  fun setTextTransform(transform: Expression.Enum<TextTransform>)

  fun setTextOffset(offset: Expression.Offset)

  fun setTextAllowOverlap(allowOverlap: Expression.Boolean)

  fun setTextOverlap(overlap: Expression.String)

  fun setTextIgnorePlacement(ignorePlacement: Expression.Boolean)

  fun setTextOptional(optional: Expression.Boolean)

  fun setTextOpacity(opacity: Expression.Number)

  fun setTextColor(color: Expression.Color)

  fun setTextHaloColor(haloColor: Expression.Color)

  fun setTextHaloWidth(haloWidth: Expression.Dp)

  fun setTextHaloBlur(haloBlur: Expression.Dp)

  fun setTextTranslate(translate: Expression.DpOffset)

  fun setTextTranslateAnchor(translateAnchor: Expression.Enum<TranslateAnchor>)
}
