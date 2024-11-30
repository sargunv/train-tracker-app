package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
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

@PublishedApi
internal expect class SymbolLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<Boolean>)

  fun setSymbolPlacement(placement: Expression<SymbolPlacement>)

  fun setSymbolSpacing(spacing: Expression<Number>)

  fun setSymbolAvoidEdges(avoidEdges: Expression<Boolean>)

  fun setSymbolSortKey(sortKey: Expression<Number>)

  fun setSymbolZOrder(zOrder: Expression<SymbolZOrder>)

  fun setIconAllowOverlap(allowOverlap: Expression<Boolean>)

  fun setIconOverlap(overlap: Expression<String>)

  fun setIconIgnorePlacement(ignorePlacement: Expression<Boolean>)

  fun setIconOptional(optional: Expression<Boolean>)

  fun setIconRotationAlignment(rotationAlignment: Expression<IconRotationAlignment>)

  fun setIconSize(size: Expression<Number>)

  fun setIconTextFit(textFit: Expression<IconTextFit>)

  fun setIconTextFitPadding(textFitPadding: Expression<Insets>)

  fun setIconImage(image: Expression<TResolvedImage>)

  fun setIconRotate(rotate: Expression<Number>)

  fun setIconPadding(padding: Expression<Number>)

  fun setIconKeepUpright(keepUpright: Expression<Boolean>)

  fun setIconOffset(offset: Expression<Point>)

  fun setIconAnchor(anchor: Expression<SymbolAnchor>)

  fun setIconPitchAlignment(pitchAlignment: Expression<IconPitchAlignment>)

  fun setIconOpacity(opacity: Expression<Number>)

  fun setIconColor(color: Expression<Color>)

  fun setIconHaloColor(haloColor: Expression<Color>)

  fun setIconHaloWidth(haloWidth: Expression<Number>)

  fun setIconHaloBlur(haloBlur: Expression<Number>)

  fun setIconTranslate(translate: Expression<Point>)

  fun setIconTranslateAnchor(translateAnchor: Expression<TranslateAnchor>)

  fun setTextPitchAlignment(pitchAlignment: Expression<TextPitchAlignment>)

  fun setTextRotationAlignment(rotationAlignment: Expression<TextRotationAlignment>)

  fun setTextField(field: Expression<TFormatted>)

  fun setTextFont(font: Expression<List<String>>)

  fun setTextSize(size: Expression<Number>)

  fun setTextMaxWidth(maxWidth: Expression<Number>)

  fun setTextLineHeight(lineHeight: Expression<Number>)

  fun setTextLetterSpacing(letterSpacing: Expression<Number>)

  fun setTextJustify(justify: Expression<TextJustify>)

  fun setTextRadialOffset(radialOffset: Expression<Number>)

  fun setTextVariableAnchor(variableAnchor: Expression<List<SymbolAnchor>>)

  fun setTextVariableAnchorOffset(variableAnchorOffset: Expression<List<Pair<SymbolAnchor, Point>>>)

  fun setTextAnchor(anchor: Expression<SymbolAnchor>)

  fun setTextMaxAngle(maxAngle: Expression<Number>)

  fun setTextWritingMode(writingMode: Expression<List<TextWritingMode>>)

  fun setTextRotate(rotate: Expression<Number>)

  fun setTextPadding(padding: Expression<Number>)

  fun setTextKeepUpright(keepUpright: Expression<Boolean>)

  fun setTextTransform(transform: Expression<TextTransform>)

  fun setTextOffset(offset: Expression<Point>)

  fun setTextAllowOverlap(allowOverlap: Expression<Boolean>)

  fun setTextOverlap(overlap: Expression<String>)

  fun setTextIgnorePlacement(ignorePlacement: Expression<Boolean>)

  fun setTextOptional(optional: Expression<Boolean>)

  fun setTextOpacity(opacity: Expression<Number>)

  fun setTextColor(color: Expression<Color>)

  fun setTextHaloColor(haloColor: Expression<Color>)

  fun setTextHaloWidth(haloWidth: Expression<Number>)

  fun setTextHaloBlur(haloBlur: Expression<Number>)

  fun setTextTranslate(translate: Expression<Point>)

  fun setTextTranslateAnchor(translateAnchor: Expression<TranslateAnchor>)
}
