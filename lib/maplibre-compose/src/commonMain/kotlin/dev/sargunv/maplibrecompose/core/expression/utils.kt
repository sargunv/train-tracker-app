package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.const
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.heatmapDensity
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.interpolate
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.linear
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.literal

// helpers for default expression values

public val ZeroPadding: PaddingValues.Absolute = PaddingValues.Absolute(0.dp)

public object Defaults {
  public val HeatmapColors: Expression<ColorValue> =
    interpolate(
      linear(),
      heatmapDensity(),
      0 to const(Color.Transparent),
      0.1 to const(Color(0xFF4169E1)), // royal blue
      0.3 to const(Color(0xFF00FFFF)), // cyan
      0.5 to const(Color(0xFF00FF00)), // lime
      0.7 to const(Color(0xFFFFFF00)), // yellow
      1 to const(Color(0xFFFF0000)), // red
    )

  public val FontNames: Expression<ListValue<StringValue>> =
    literal(listOf(const("Open Sans Regular"), const("Arial Unicode MS Regular")))
}
