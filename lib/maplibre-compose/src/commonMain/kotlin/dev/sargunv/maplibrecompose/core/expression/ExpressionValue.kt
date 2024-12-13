package dev.sargunv.maplibrecompose.core.expression

/** Represents a value that an expression can resolve to. */
public sealed interface ExpressionValue

/** Represents an expression that resolves to a true or false value. */
public sealed interface BooleanValue : ExpressionValue

/**
 * Represents an expression that resolves to any numeric quantity. Corresponds to numbers in the
 * JSON style spec.
 */
public sealed interface ScalarValue :
  ExpressionValue, MatchableValue, InterpolateableValue, ComparableValue

/** Represents an expression that resolves to an integer quantity. */
public sealed interface IntScalarValue : ScalarValue

/** Represents an expression that resolves to a floating-point quantity. */
public sealed interface FloatScalarValue : ScalarValue

/** Represents an expression that resolves to any dimensionless quantity. */
public sealed interface NumberValue : ScalarValue

/** Represents an expression that resolves to a floating-point dimensionless quantity. */
public sealed interface FloatValue : NumberValue, FloatScalarValue

/** Represents an expression that resolves to an integer dimensionless quantity. */
public sealed interface IntValue : NumberValue, IntScalarValue

/** Represents an expression that resolves to device-independent pixels (dp). */
public sealed interface DpValue : FloatScalarValue

/** Represents an expression that resolves to a string value. */
public sealed interface StringValue : ExpressionValue, MatchableValue, ComparableValue

/** Represents an expression that resolves to an enum value of type [T]. */
public sealed interface EnumValue<out T : LayerPropertyEnum> : StringValue

/** Represents an expression that resolves to a color value. */
public sealed interface ColorValue : ExpressionValue, InterpolateableValue

/** Represents an expression that resolves to a map value (corresponds to a JSON object). */
public sealed interface MapValue : ExpressionValue

/** Represents an expression that resolves to a list value (corresponds to a JSON array). */
public sealed interface ListValue<out T : ExpressionValue> : ExpressionValue

/** Represents an expression that resolves to a list of scalar values. */
public sealed interface VectorValue : ListValue<ScalarValue>, InterpolateableValue

/** Represents an expression that resolves to a 2D floating point offset in physical pixels. */
public sealed interface OffsetValue : VectorValue

/**
 * Represents an expression that resolves to a 2D floating point offset in device-independent
 * pixels.
 */
public sealed interface DpOffsetValue : VectorValue

/**
 * Represents an expression that resolves to an absolute (layout direction unaware) padding applied
 * along the edges inside a box.
 */
public sealed interface PaddingValue : VectorValue

/**
 * Represents an expression that resolves to a collator object for use in locale-dependent
 * comparison operations. See [ExpressionScope.collator].
 */
public sealed interface CollatorValue : ExpressionValue

/** Represents an expression that resolves to a formatted string. See [ExpressionScope.format]. */
public sealed interface FormattedValue : ExpressionValue

/** Represents an expression that resolves to a geometry object. */
public sealed interface GeoJsonValue : ExpressionValue

/** Represents an expression that resolves to an image. See [ExpressionScope.image]. */
public sealed interface ImageValue : ExpressionValue

/** Represents an expression that resolves to an interpolation type. */
public sealed interface InterpolationValue : ExpressionValue

/**
 * Represents an expression that resolves to a value that can be matched. See
 * [ExpressionScope.match].
 */
public sealed interface MatchableValue : ExpressionValue

/**
 * Represents an expression that resolves to a value that can be ordered with other values of its
 * type.
 */
public sealed interface ComparableValue : ExpressionValue

/**
 * Represents an expression that resolves to a value that can be interpolated. See
 * [ExpressionScope.interpolate].
 */
public sealed interface InterpolateableValue : ExpressionValue
