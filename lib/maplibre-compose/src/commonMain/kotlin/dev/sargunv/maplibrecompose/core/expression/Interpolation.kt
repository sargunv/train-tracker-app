package dev.sargunv.maplibrecompose.core.expression

import dev.sargunv.maplibrecompose.core.expression.Expressions.const

public sealed interface Interpolation : Expression {

  /** Interpolates linearly between the pairs of stops. */
  public data object Linear : Interpolation by ExpressionImpl.ofString("linear")

  /**
   * Interpolates exponentially between the stops.
   *
   * @param [base] controls the rate at which the output increases: higher values make the output
   *   increase more towards the high end of the range. With values close to 1 the output increases
   *   linearly.
   */
  public class Exponential(base: FloatExpression) :
    Interpolation by ExpressionImpl.ofList(listOf(const("exponential"), base))

  /**
   * Interpolates using the cubic bezier curve defined by the given control points between the pairs
   * of stops.
   */
  public class CubicBezier(
    x1: FloatExpression,
    y1: FloatExpression,
    x2: FloatExpression,
    y2: FloatExpression,
  ) : Interpolation by ExpressionImpl.ofList(listOf(const("cubic-bezier"), x1, y1, x2, y2))
}
