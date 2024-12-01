package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

// token types for expression type safety; these should never be instantiated

public sealed interface TFormatted

public sealed interface TResolvedImage

public sealed interface TCollator

public sealed interface TInterpolationType

public val ZeroPadding: PaddingValues.Absolute = PaddingValues.Absolute(0.dp)
