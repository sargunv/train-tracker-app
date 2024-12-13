package dev.sargunv.maplibrecompose.core.expression

public sealed interface Expression {
  public val value: Any?

  /** Represents an expression that resolves to a true or false value. */
  public sealed interface Boolean : Expression

  /**
   * Represents an expression that resolves to any numeric quantity. Corresponds to numbers in the
   * JSON style spec.
   */
  public sealed interface Scalar : Expression, Matchable, Interpolateable, Comparable

  /** Represents an expression that resolves to an integer quantity. */
  public sealed interface IntScalar : Scalar

  /** Represents an expression that resolves to a floating-point quantity. */
  public sealed interface FloatScalar : Scalar

  /** Represents an expression that resolves to any dimensionless quantity. */
  public sealed interface Number : Scalar

  /** Represents an expression that resolves to a floating-point dimensionless quantity. */
  public sealed interface Float : Number, FloatScalar

  /** Represents an expression that resolves to an integer dimensionless quantity. */
  public sealed interface Int : Number, IntScalar

  /** Represents an expression that resolves to device-independent pixels (dp). */
  public sealed interface Dp : FloatScalar

  /** Represents an expression that resolves to a string value. */
  public sealed interface String : Expression, Matchable, Comparable

  /** Represents an expression that resolves to an enum value of type [T]. */
  public sealed interface Enum<out T : LayerPropertyEnum> : String

  /** Represents an expression that resolves to a color value. */
  public sealed interface Color : Expression, Interpolateable

  /** Represents an expression that resolves to a map value (corresponds to a JSON object). */
  public sealed interface Map : Expression

  /** Represents an expression that resolves to a list value (corresponds to a JSON array). */
  public sealed interface List : Expression // TODO make it generic again

  /** Represents an expression that resolves to a list of scalar values. */
  public sealed interface Vector : List, Interpolateable

  /** Represents an expression that resolves to a 2D floating point offset in physical pixels. */
  public sealed interface Offset : Vector

  /**
   * Represents an expression that resolves to a 2D floating point offset in device-independent
   * pixels.
   */
  public sealed interface DpOffset : Vector

  /**
   * Represents an expression that resolves to an absolute (RTL unaware) padding applied along the
   * edges inside a box.
   */
  public sealed interface Padding : Vector

  /** Represents an expression that resolves to a formatted string. See [ExpressionScope.format]. */
  public sealed interface Formatted : Expression

  /** Represents an expression that resolves to an image. See [ExpressionScope.image]. */
  public sealed interface ResolvedImage : Expression

  /** Represents an expression that resolves to a geometry object. */
  public sealed interface GeoJson : Expression

  /**
   * Represents an expression that resolves to a collator object for use in locale-dependent
   * comparison operations. See [ExpressionScope.collator].
   */
  public sealed interface Collator : Expression

  /**
   * Represents an expression that resolves to a value that can be interpolated. See
   * [ExpressionScope.interpolate].
   */
  public sealed interface Interpolateable : Expression

  /**
   * Represents an expression that resolves to a value that can be matched. See
   * [ExpressionScope.match].
   */
  public sealed interface Matchable : Expression

  /**
   * Represents an expression that resolves to a value that can be ordered with other values of its
   * type.
   */
  public sealed interface Comparable : Expression

  /**
   * An implementation of [Expression] that wraps a JSON-like value and is assignable to all kinds
   * of expressions. It's the implementation returned by non-const expression DSL functions (see
   * [ExpressionScope]), allowing you to do a checked cast to other expression types.
   */
  public class Impl internal constructor(override val value: Any?) :
    Boolean,
    Float,
    Int,
    Dp,
    String,
    Enum<LayerPropertyEnum>,
    Color,
    Map,
    List,
    Vector,
    Offset,
    DpOffset,
    Padding,
    Formatted,
    ResolvedImage,
    Collator,
    Interpolation {
    internal inline fun <reified T : Expression> cast(): T = this as T

    internal companion object {
      fun ofList(value: kotlin.collections.List<Expression>): Impl = Impl(value.map { it.value })

      fun ofMap(value: kotlin.collections.Map<kotlin.String, Expression>): Impl =
        Impl(value.mapValues { it.value.value })
    }
  }
}

public sealed interface Interpolation : Expression {
  /** Interpolates linearly between the pairs of stops. */
  public data object Linear : Interpolation by Expression.Impl("linear")

  /**
   * Interpolates exponentially between the stops. [base] controls the rate at which the output
   * increases: higher values make the output increase more towards the high end of the range. With
   * values close to 1 the output increases linearly.
   */
  public class Exponential(base: Expression.Float) :
    Interpolation by Expression.Impl(listOf("exponential", base.value))

  /**
   * Interpolates using the cubic bezier curve defined by the given control points between the pairs
   * of stops.
   */
  public class CubicBezier(
    x1: Expression.Float,
    y1: Expression.Float,
    x2: Expression.Float,
    y2: Expression.Float,
  ) :
    Interpolation by Expression.Impl(
      listOf("cubic-bezier", x1.value, y1.value, x2.value, y2.value)
    )
}
