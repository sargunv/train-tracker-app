package dev.sargunv.maplibrekmp.core.layer

import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrekmp.core.source.Source
import dev.sargunv.maplibrekmp.expression.Expression
import dev.sargunv.maplibrekmp.expression.Insets
import dev.sargunv.maplibrekmp.expression.Point
import dev.sargunv.maplibrekmp.expression.TFormatted
import dev.sargunv.maplibrekmp.expression.TResolvedImage

@PublishedApi
internal expect class SymbolLayer(id: String, source: Source) : FeatureLayer {
  fun setSymbolPlacement(placement: Expression<String>)

  fun setSymbolSpacing(spacing: Expression<Number>)

  fun setSymbolAvoidEdges(avoidEdges: Expression<Boolean>)

  fun setSymbolSortKey(sortKey: Expression<Number>)

  fun setSymbolZOrder(zOrder: Expression<String>)

  fun setIconAllowOverlap(allowOverlap: Expression<Boolean>)

  fun setIconOverlap(overlap: Expression<String>)

  fun setIconIgnorePlacement(ignorePlacement: Expression<Boolean>)

  fun setIconOptional(optional: Expression<Boolean>)

  fun setIconRotationAlignment(rotationAlignment: Expression<String>)

  fun setIconSize(size: Expression<Number>)

  fun setIconTextFit(textFit: Expression<String>)

  fun setIconTextFitPadding(textFitPadding: Expression<Insets>)

  fun setIconImage(image: Expression<TResolvedImage>)

  fun setIconRotate(rotate: Expression<Number>)

  fun setIconPadding(padding: Expression<Number>)

  fun setIconKeepUpright(keepUpright: Expression<Boolean>)

  fun setIconOffset(offset: Expression<Point>)

  fun setIconAnchor(anchor: Expression<String>)

  fun setIconPitchAlignment(pitchAlignment: Expression<String>)

  fun setIconOpacity(opacity: Expression<Number>)

  fun setIconColor(color: Expression<Color>)

  fun setIconHaloColor(haloColor: Expression<Color>)

  fun setIconHaloWidth(haloWidth: Expression<Number>)

  fun setIconHaloBlur(haloBlur: Expression<Number>)

  fun setIconTranslate(translate: Expression<Point>)

  fun setIconTranslateAnchor(translateAnchor: Expression<String>)

  fun setTextPitchAlignment(pitchAlignment: Expression<String>)

  fun setTextRotationAlignment(rotationAlignment: Expression<String>)

  fun setTextField(field: Expression<TFormatted>)

  fun setTextFont(font: Expression<List<String>>)

  fun setTextSize(size: Expression<Number>)

  fun setTextMaxWidth(maxWidth: Expression<Number>)

  fun setTextLineHeight(lineHeight: Expression<Number>)

  fun setTextLetterSpacing(letterSpacing: Expression<Number>)

  fun setTextJustify(justify: Expression<String>)

  fun setTextRadialOffset(radialOffset: Expression<Number>)

  fun setTextVariableAnchor(variableAnchor: Expression<List<String>>)

  fun setTextVariableAnchorOffset(variableAnchorOffset: Expression<List<*>>)

  fun setTextAnchor(anchor: Expression<String>)

  fun setTextMaxAngle(maxAngle: Expression<Number>)

  fun setTextWritingMode(writingMode: Expression<List<String>>)

  fun setTextRotate(rotate: Expression<Number>)

  fun setTextPadding(padding: Expression<Number>)

  fun setTextKeepUpright(keepUpright: Expression<Boolean>)

  fun setTextTransform(transform: Expression<String>)

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

  fun setTextTranslateAnchor(translateAnchor: Expression<String>)
}
