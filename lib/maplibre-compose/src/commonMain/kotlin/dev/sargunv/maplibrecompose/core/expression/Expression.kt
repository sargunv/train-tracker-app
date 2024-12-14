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
    private val ofEmptyMap = Expression<MapValue<Nothing>>(emptyMap<String, Nothing>())
    private val ofEmptyList = Expression<ListValue<Nothing>>(emptyList<Nothing>())
    private val ofZeroOffset = Expression<OffsetValue>(Offset.Zero)
    private val ofZeroPadding = Expression<PaddingValue>(ZeroPadding)

    val ofNull = Expression<ExpressionValue>(null)
    val ofTrue = Expression<BooleanValue>(true)
    val ofFalse = Expression<BooleanValue>(false)

    private fun Float.isSmallInt() = toInt().toFloat() == this && toInt().isSmallInt()

    private fun Int.isSmallInt() = this in 0..<NUM_SMALL_NUMBERS

    fun ofFloat(float: Float) =
      when {
        float.isSmallInt() -> constSmallInts[float.toInt()]
        (float / SMALL_FLOAT_RESOLUTION).isSmallInt() ->
          constSmallFloats[(float / SMALL_FLOAT_RESOLUTION).toInt()]

        else -> Expression(float)
      }

    fun ofInt(int: Int) = if (int.isSmallInt()) constSmallInts[int] else Expression(int)

    fun ofString(string: String) = if (string.isEmpty()) ofEmptyString else Expression(string)

    fun ofColor(color: Color) =
      when (color) {
        Color.Transparent -> ofTransparent
        Color.Black -> ofBlack
        Color.White -> ofWhite
        else -> Expression(color)
      }

    fun <T : ExpressionValue> ofMap(map: Map<String, Expression<T>>): Expression<MapValue<T>> =
      if (map.isEmpty()) ofEmptyMap else Expression(map.mapValues { it.value.value })

    fun <T : ExpressionValue> ofList(list: List<Expression<T>>): Expression<ListValue<T>> =
      if (list.isEmpty()) ofEmptyList else Expression(list.map { it.value })

    fun ofOffset(offset: Offset) = if (offset == Offset.Zero) ofZeroOffset else Expression(offset)

    fun ofPadding(padding: PaddingValues.Absolute) =
      if (padding == ZeroPadding) ofZeroPadding else Expression(padding)
  }
}
