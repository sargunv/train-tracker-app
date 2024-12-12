package dev.sargunv.maplibrecompose.core.expression

public sealed interface Expression {
  public val value: Any?

  public sealed interface Boolean : Expression

  public sealed interface Scalar : Expression, Matchable, Interpolateable, Comparable

  public sealed interface Number : Scalar

  // TODO make Dp extend Scalar instead of Number (so you can't pass Dp to non-dp expressions)
  // need to update certain expression functions to accept Scalar instead of Number
  public sealed interface Dp : Number

  public sealed interface String : Expression, Matchable, Comparable

  public sealed interface Enum<out T : LayerPropertyEnum> : String

  public sealed interface Color : Expression, Interpolateable

  public sealed interface Map : Expression

  public sealed interface List : Expression // TODO make it generic again

  public sealed interface Vector : List, Interpolateable

  public sealed interface Offset : Vector

  public sealed interface DpOffset : Vector

  public sealed interface Padding : Vector

  public sealed interface Formatted : Expression

  public sealed interface ResolvedImage : Expression

  public sealed interface GeoJson : Expression

  public sealed interface Collator : Expression

  public sealed interface Interpolateable : Expression

  public sealed interface Matchable : Expression

  public sealed interface Comparable : Expression

  /**
   * An implementation of [Expression] that wraps a JSON-like value and is assignable to all kinds
   * of expressions. It's the implementation returned by non-const expression DSL functions (see
   * [ExpressionScope]), allowing you to do a checked cast to other expression types.
   */
  public class Impl internal constructor(override val value: Any?) :
    Boolean,
    Number,
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
  public class Exponential(base: Expression.Number) :
    Interpolation by Expression.Impl(listOf("exponential", base.value))

  /**
   * Interpolates using the cubic bezier curve defined by the given control points between the pairs
   * of stops.
   */
  public class CubicBezier(
    x1: Expression.Number,
    y1: Expression.Number,
    x2: Expression.Number,
    y2: Expression.Number,
  ) :
    Interpolation by Expression.Impl(
      listOf("cubic-bezier", x1.value, y1.value, x2.value, y2.value)
    )
}
