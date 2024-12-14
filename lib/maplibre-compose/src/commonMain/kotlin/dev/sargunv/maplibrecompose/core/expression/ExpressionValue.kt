package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import kotlin.time.Duration

/**
 * Represents a value that an [Expression] can resolve to. These types are never actually
 * instantiated at runtime; they're only used as type parameters to hint at the type of an
 * [Expression]
 */
public sealed interface ExpressionValue

/**
 * Represents an [Expression] that resolves to a true or false value. See [ExpressionScope.const].
 */
public sealed interface BooleanValue : ExpressionValue, EquatableValue

/**
 * Represents an [Expression] that resolves to a numeric quantity. Corresponds to numbers in the
 * JSON style spec. Use [ExpressionScope.const] to create a literal [ScalarValue].
 *
 * @param U the unit type of the scalar value. For dimensionless quantities, use [Unit].
 */
public sealed interface ScalarValue<U> :
  ExpressionValue,
  MatchableValue,
  InterpolateableValue<U>,
  ComparableValue<ScalarValue<U>>,
  EquatableValue

/**
 * Represents an [Expression] that resolves to a dimensionless quantity. See
 * [ExpressionScope.const].
 */
public typealias FloatValue = ScalarValue<Unit>

/**
 * Represents an [Expression] that resolves to an integer dimensionless quantity. See
 * [ExpressionScope.const].
 */
public sealed interface IntValue : ScalarValue<Unit>

/**
 * Represents an [Expression] that resolves to device-independent pixels ([Dp]). See
 * [ExpressionScope.const].
 */
public typealias DpValue = ScalarValue<Dp>

/**
 * Represents an [Expression] that resolves to an amount of time with millisecond precision
 * ([Duration]). See [ExpressionScope.const].
 */
public typealias DurationValue = ScalarValue<Duration>

/** Represents an [Expression] that resolves to a string value. See [ExpressionScope.const]. */
public sealed interface StringValue :
  ExpressionValue, MatchableValue, ComparableValue<StringValue>, EquatableValue

/**
 * Represents an [Expression] that resolves to a [LayerPropertyEnum] value of type [T]. See
 * [ExpressionScope.const].
 *
 * @param T The [LayerPropertyEnum] type that this value represents.
 */
public sealed interface EnumValue<out T : LayerPropertyEnum> : StringValue

/** Represents an [Expression] that resolves to a [Color] value. See [ExpressionScope.const]. */
public sealed interface ColorValue : ExpressionValue, InterpolateableValue<ColorValue>

/**
 * Represents an [Expression] that resolves to a map value (corresponds to a JSON object). See
 * [ExpressionScope.literal].
 */
public sealed interface MapValue : ExpressionValue

/**
 * Represents an [Expression] that resolves to a list value (corresponds to a JSON array). See
 * [ExpressionScope.literal].
 */
public sealed interface ListValue<out T : ExpressionValue> : ExpressionValue

/**
 * Represents an [Expression] that resolves to a list of scalar values.
 *
 * @param U the unit type of the scalar values. For dimensionless quantities, use [Unit].
 */
public sealed interface VectorValue<U> :
  ListValue<ScalarValue<U>>, InterpolateableValue<VectorValue<U>>

/**
 * Represents an [Expression] that resolves to a 2D floating point offset in physical pixels
 * ([Offset]). See [ExpressionScope.const].
 */
public sealed interface OffsetValue : VectorValue<Unit>

/**
 * Represents an [Expression] that resolves to a 2D floating point offset in device-independent
 * pixels ([DpOffset]). See [ExpressionScope.const].
 */
public sealed interface DpOffsetValue : VectorValue<Dp>

/**
 * Represents an [Expression] that resolves to an absolute (layout direction unaware) padding
 * applied along the edges inside a box ([PaddingValues.Absolute]). See [ExpressionScope.const].
 */
public sealed interface PaddingValue : VectorValue<Dp>

/**
 * Represents an [Expression] that resolves to a collator object for use in locale-dependent
 * comparison operations. See [ExpressionScope.collator].
 */
public sealed interface CollatorValue : ExpressionValue

/** Represents an [Expression] that resolves to a formatted string. See [ExpressionScope.format]. */
public sealed interface FormattedValue : ExpressionValue

/** Represents an [Expression] that resolves to a geometry object. */
public sealed interface GeoJsonValue : ExpressionValue

/** Represents an [Expression] that resolves to an image. See [ExpressionScope.image]. */
public sealed interface ImageValue : ExpressionValue

/**
 * Represents an [Expression] that resolves to an interpolation type. See [ExpressionScope.linear],
 * [ExpressionScope.exponential], and [ExpressionScope.cubicBezier].
 */
public sealed interface InterpolationValue : ExpressionValue

/**
 * Represents an [Expression] that resolves to a value that can be compared for equality. See
 * [ExpressionScope.eq] and [ExpressionScope.neq].
 */
public sealed interface EquatableValue : ExpressionValue

/**
 * Union type for an [Expression] that resolves to a value that can be matched. See
 * [ExpressionScope.match].
 */
public sealed interface MatchableValue : ExpressionValue

/**
 * Union type for an [Expression] that resolves to a value that can be ordered with other values of
 * its type. See [ExpressionScope.gt], [ExpressionScope.lt], [ExpressionScope.gte], and
 * [ExpressionScope.lte].
 *
 * @param T the type of the value that can be compared against for ordering.
 */
public sealed interface ComparableValue<T> : ExpressionValue

/**
 * Union type for an [Expression] that resolves to a value that can be interpolated. See
 * [ExpressionScope.interpolate].
 *
 * @param T the type of values that can be interpolated between.
 */
public sealed interface InterpolateableValue<T> : ExpressionValue
