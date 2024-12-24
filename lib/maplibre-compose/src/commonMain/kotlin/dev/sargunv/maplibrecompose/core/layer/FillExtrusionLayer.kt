package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.ImageValue
import dev.sargunv.maplibrecompose.core.expression.ResolvedValue
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.source.Source

internal expect class FillExtrusionLayer(id: String, source: Source) : FeatureLayer {
  override var sourceLayer: String

  override fun setFilter(filter: Expression<BooleanValue>)

  fun setFillExtrusionOpacity(opacity: Expression<FloatValue>)

  fun setFillExtrusionColor(color: Expression<ColorValue>)

  fun setFillExtrusionTranslate(translate: Expression<DpOffsetValue>)

  fun setFillExtrusionTranslateAnchor(anchor: Expression<EnumValue<TranslateAnchor>>)

  fun setFillExtrusionPattern(pattern: Expression<ResolvedValue<ImageValue>>)

  fun setFillExtrusionHeight(height: Expression<FloatValue>)

  fun setFillExtrusionBase(base: Expression<FloatValue>)

  fun setFillExtrusionVerticalGradient(verticalGradient: Expression<BooleanValue>)
}
