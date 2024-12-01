package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable

// token types for expression type safety; these should never be instantiated

public sealed interface TFormatted

public sealed interface TResolvedImage

public sealed interface TCollator

public sealed interface TInterpolationType

public sealed interface TGeometry // TODO create a real type and Expression constructor

@Immutable
public data class Insets(val top: Number, val right: Number, val bottom: Number, val left: Number)
