package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.TextUnit
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.const
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.div
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.resolveTextUnits
import dev.sargunv.maplibrecompose.core.expression.FloatValue

@Composable
internal fun Expression<*>.rememberTextUnitsAsSp(
  spPerEm: Expression<FloatValue>,
  unspecified: TextUnit,
) =
  remember(this, spPerEm, unspecified) {
    resolveTextUnits(
      spMultiplier = const(1f),
      emMultiplier = spPerEm,
      unspecifiedValue = const(unspecified),
    )
  }

@Composable
internal fun Expression<*>.rememberTextUnitsAsEm(
  spPerEm: Expression<FloatValue>,
  unspecified: TextUnit,
) =
  remember(this, spPerEm, unspecified) {
    resolveTextUnits(
      spMultiplier = const(1f) / spPerEm,
      emMultiplier = const(1f),
      unspecifiedValue = const(unspecified),
    )
  }
