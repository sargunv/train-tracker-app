package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

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

public sealed interface Interpolation : Expression {

  /** Interpolates linearly between the pairs of stops. */
  public data object Linear : Interpolation by ExpressionImpl("linear")

  /**
   * Interpolates exponentially between the stops.
   *
   * @param [base] controls the rate at which the output increases: higher values make the output
   *   increase more towards the high end of the range. With values close to 1 the output increases
   *   linearly.
   */
  public class Exponential(base: FloatExpression) :
    Interpolation by ExpressionImpl(listOf("exponential", base.value))

  /**
   * Interpolates using the cubic bezier curve defined by the given control points between the pairs
   * of stops.
   */
  public class CubicBezier(
    x1: FloatExpression,
    y1: FloatExpression,
    x2: FloatExpression,
    y2: FloatExpression,
  ) :
    Interpolation by ExpressionImpl(listOf("cubic-bezier", x1.value, y1.value, x2.value, y2.value))
}

internal class ExpressionImpl(override val value: Any?) :
  BooleanExpression,
  FloatExpression,
  IntExpression,
  DpExpression,
  StringExpression,
  EnumExpression<LayerPropertyEnum>,
  ColorExpression,
  MapExpression,
  VectorExpression, // also provides ListExpression<*> (with type erasure)
  OffsetExpression,
  DpOffsetExpression,
  PaddingExpression,
  FormattedExpression,
  ImageExpression,
  CollatorExpression,
  Interpolation {
  internal inline fun <reified T : Expression> cast(): T = this as T

  internal companion object {
    val ofNull = ExpressionImpl(null)
    val ofTrue = ExpressionImpl(true)
    val ofFalse = ExpressionImpl(false)

    private val constSmallInts = Array(512) { ExpressionImpl(it.toFloat()) }
    private val constSmallFloats = Array(512) { ExpressionImpl(it.toFloat() / 20f) }
    private val ofEmptyString = ExpressionImpl("")
    private val ofTransparent = ExpressionImpl(Color.Transparent)
    private val ofBlack = ExpressionImpl(Color.Black)
    private val ofWhite = ExpressionImpl(Color.White)
    private val ofEmptyList = ExpressionImpl(emptyList<Any?>())
    private val ofZeroOffset = ExpressionImpl(Offset.Zero)
    private val ofZeroPadding = ExpressionImpl(ZeroPadding)

    private val ofEmptyMap = ExpressionImpl(emptyMap<String, Any?>())

    private fun isSmallInt(f: Float) = f >= 0 && f < 512 && f.toInt().toFloat() == f

    fun ofFloat(float: Float) =
      when {
        isSmallInt(float) -> constSmallInts[float.toInt()]
        isSmallInt(float * 20f) -> constSmallFloats[(float * 20f).toInt()]
        else -> ExpressionImpl(float)
      }

    fun ofString(string: String) = if (string.isEmpty()) ofEmptyString else ExpressionImpl(string)

    fun ofColor(color: Color) =
      when (color) {
        Color.Transparent -> ofTransparent
        Color.Black -> ofBlack
        Color.White -> ofWhite
        else -> ExpressionImpl(color)
      }

    fun ofMap(map: Map<String, Expression>) =
      if (map.isEmpty()) ofEmptyMap else ExpressionImpl(map.mapValues { it.value.value })

    fun ofList(list: List<Expression>) =
      if (list.isEmpty()) ofEmptyList else ExpressionImpl(list.map { it.value })

    fun ofOffset(offset: Offset) =
      if (offset == Offset.Zero) ofZeroOffset else ExpressionImpl(offset)

    fun ofPadding(padding: PaddingValues.Absolute) =
      if (padding == ZeroPadding) ofZeroPadding else ExpressionImpl(padding)
  }
}
