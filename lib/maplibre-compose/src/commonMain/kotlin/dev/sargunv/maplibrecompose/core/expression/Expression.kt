package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/** Wraps a JSON-like value that represents an expression, typically used for styling map layers. */
public data class Expression<@Suppress("unused") out T : ExpressionValue>
private constructor(
  /** The JSON-like value that backs this expression. */
  public val value: Any?
) {
  @Suppress("UNCHECKED_CAST")
  internal fun <T : ExpressionValue> cast(): Expression<T> = this as Expression<T>

  internal companion object {
    private const val NUM_SMALL_NUMBERS = 512
    private const val SMALL_FLOAT_RESOLUTION = 0.05f

    private val constSmallInts = Array(NUM_SMALL_NUMBERS) { Expression<IntValue>(it) }
    private val constSmallFloats =
      Array(NUM_SMALL_NUMBERS) { Expression<FloatValue>(it.toFloat() / 20f) }
    private val ofEmptyString = Expression<StringValue>("")
    private val ofTransparent = Expression<ColorValue>(Color.Transparent)
    private val ofBlack = Expression<ColorValue>(Color.Black)
    private val ofWhite = Expression<ColorValue>(Color.White)
    private val ofEmptyMap = Expression<MapValue>(emptyMap<String, Any?>())
    private val ofEmptyList = Expression<ListValue<*>>(emptyList<Any?>())
    private val ofZeroOffset = Expression<OffsetValue>(Offset.Zero)
    private val ofZeroPadding = Expression<PaddingValue>(ZeroPadding)

    val ofNull = Expression<ExpressionValue>(null)
    val ofTrue = Expression<BooleanValue>(true)
    val ofFalse = Expression<BooleanValue>(false)

    private fun Float.isSmallInt() = toInt().toFloat() == this && toInt().isSmallInt()

    private fun Int.isSmallInt() = this in 0..<NUM_SMALL_NUMBERS

    fun <T : FloatScalarValue> ofFloat(float: Float): Expression<T> =
      when {
        float.isSmallInt() -> constSmallInts[float.toInt()]
        (float / SMALL_FLOAT_RESOLUTION).isSmallInt() ->
          constSmallFloats[(float / SMALL_FLOAT_RESOLUTION).toInt()]

        else -> Expression<FloatValue>(float)
      }.cast()

    fun <T : IntScalarValue> ofInt(int: Int): Expression<T> =
      (if (int.isSmallInt()) constSmallInts[int] else Expression(int)).cast()

    fun ofString(string: String): StringExpression =
      if (string.isEmpty()) ofEmptyString else Expression(string)

    fun ofColor(color: Color): ColorExpression =
      when (color) {
        Color.Transparent -> ofTransparent
        Color.Black -> ofBlack
        Color.White -> ofWhite
        else -> Expression(color)
      }

    fun ofMap(map: Map<String, Expression<*>>): MapExpression =
      if (map.isEmpty()) ofEmptyMap else Expression(map.mapValues { it.value.value })

    fun <T : ExpressionValue> ofList(list: List<Expression<T>>): ListExpression<T> =
      if (list.isEmpty()) ofEmptyList.cast() else Expression(list.map { it.value })

    fun ofOffset(offset: Offset) = if (offset == Offset.Zero) ofZeroOffset else Expression(offset)

    fun ofPadding(padding: PaddingValues.Absolute) =
      if (padding == ZeroPadding) ofZeroPadding else Expression(padding)
  }
}

// TODO inline these
public typealias BooleanExpression = Expression<BooleanValue>

public typealias ScalarExpression = Expression<ScalarValue>

public typealias IntScalarExpression = Expression<IntScalarValue>

public typealias FloatScalarExpression = Expression<FloatScalarValue>

public typealias NumberExpression = Expression<NumberValue>

public typealias FloatExpression = Expression<FloatValue>

public typealias IntExpression = Expression<IntValue>

public typealias DpExpression = Expression<DpValue>

public typealias StringExpression = Expression<StringValue>

public typealias EnumExpression<T> = Expression<EnumValue<T>>

public typealias ColorExpression = Expression<ColorValue>

public typealias MapExpression = Expression<MapValue>

public typealias ListExpression<T> = Expression<ListValue<T>>

public typealias VectorExpression = Expression<VectorValue>

public typealias OffsetExpression = Expression<OffsetValue>

public typealias DpOffsetExpression = Expression<DpOffsetValue>

public typealias PaddingExpression = Expression<PaddingValue>

public typealias CollatorExpression = Expression<CollatorValue>

public typealias FormattedExpression = Expression<FormattedValue>

public typealias GeoJsonExpression = Expression<GeoJsonValue>

public typealias ImageExpression = Expression<ImageValue>

public typealias Interpolation = Expression<InterpolationValue>

public typealias MatchableExpression = Expression<MatchableValue>

public typealias ComparableExpression = Expression<ComparableValue>

public typealias InterpolateableExpression = Expression<InterpolateableValue>
