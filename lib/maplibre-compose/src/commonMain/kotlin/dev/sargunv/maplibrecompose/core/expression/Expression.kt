package dev.sargunv.maplibrecompose.core.expression

/** Wraps a JSON-like value that represents an expression, typically used for styling map layers. */
public sealed interface Expression {
  /** The JSON-like value that backs this expression. */
  public val value: Any?
}

/** Represents an expression that resolves to a true or false value. */
public sealed interface BooleanExpression : Expression

/**
 * Represents an expression that resolves to any numeric quantity. Corresponds to numbers in the
 * JSON style spec.
 */
public sealed interface ScalarExpression :
  Expression, MatchableExpression, InterpolateableExpression, ComparableExpression

/** Represents an expression that resolves to an integer quantity. */
public sealed interface IntScalarExpression : ScalarExpression

/** Represents an expression that resolves to a floating-point quantity. */
public sealed interface FloatScalarExpression : ScalarExpression

/** Represents an expression that resolves to any dimensionless quantity. */
public sealed interface NumberExpression : ScalarExpression

/** Represents an expression that resolves to a floating-point dimensionless quantity. */
public sealed interface FloatExpression : NumberExpression, FloatScalarExpression

/** Represents an expression that resolves to an integer dimensionless quantity. */
public sealed interface IntExpression : NumberExpression, IntScalarExpression

/** Represents an expression that resolves to device-independent pixels (dp). */
public sealed interface DpExpression : FloatScalarExpression

/** Represents an expression that resolves to a string value. */
public sealed interface StringExpression : Expression, MatchableExpression, ComparableExpression

/** Represents an expression that resolves to an enum value of type [T]. */
public sealed interface EnumExpression<out T : LayerPropertyEnum> : StringExpression

/** Represents an expression that resolves to a color value. */
public sealed interface ColorExpression : Expression, InterpolateableExpression

/** Represents an expression that resolves to a map value (corresponds to a JSON object). */
public sealed interface MapExpression : Expression

/** Represents an expression that resolves to a list value (corresponds to a JSON array). */
public sealed interface ListExpression<out T : Expression> : Expression

/** Represents an expression that resolves to a list of scalar values. */
public sealed interface VectorExpression :
  ListExpression<ScalarExpression>, InterpolateableExpression

/** Represents an expression that resolves to a 2D floating point offset in physical pixels. */
public sealed interface OffsetExpression : VectorExpression

/**
 * Represents an expression that resolves to a 2D floating point offset in device-independent
 * pixels.
 */
public sealed interface DpOffsetExpression : VectorExpression

/**
 * Represents an expression that resolves to an absolute (RTL unaware) padding applied along the
 * edges inside a box.
 */
public sealed interface PaddingExpression : VectorExpression

/**
 * Represents an expression that resolves to a collator object for use in locale-dependent
 * comparison operations. See [ExpressionScope.collator].
 */
public sealed interface CollatorExpression : Expression

/** Represents an expression that resolves to a formatted string. See [ExpressionScope.format]. */
public sealed interface FormattedExpression : Expression

/** Represents an expression that resolves to a geometry object. */
public sealed interface GeoJsonExpression : Expression

/** Represents an expression that resolves to an image. See [ExpressionScope.image]. */
public sealed interface ImageExpression : Expression

/**
 * Represents an expression that resolves to a value that can be matched. See
 * [ExpressionScope.match].
 */
public sealed interface MatchableExpression : Expression

/**
 * Represents an expression that resolves to a value that can be ordered with other values of its
 * type.
 */
public sealed interface ComparableExpression : Expression

/**
 * Represents an expression that resolves to a value that can be interpolated. See
 * [ExpressionScope.interpolate].
 */
public sealed interface InterpolateableExpression : Expression
