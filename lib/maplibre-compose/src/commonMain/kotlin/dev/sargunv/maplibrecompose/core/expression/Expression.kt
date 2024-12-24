package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

/** Wraps a JSON-like value that represents an expression, typically used for styling map layers. */
public data class Expression<@Suppress("unused") out T : ExpressionValue>
private constructor(
  /** The JSON-like value that backs this expression. */
  public val value: Any?
) {
  internal fun mapLeaves(transform: (Any?) -> Any?): Expression<*> {
    fun mapLeavesImpl(value: Any?, transform: (Any?) -> Any?): Any? {
      return when (value) {
        is Map<*, *> -> value.mapValues { (_, v) -> mapLeavesImpl(v, transform) }
        is List<*> -> value.map { v -> mapLeavesImpl(v, transform) }
        else -> transform(value)
      }
    }

    return Expression<ExpressionValue>(mapLeavesImpl(value, transform))
  }

  internal fun visitLeaves(action: (Any?) -> Unit) {
    fun visitLeavesImpl(value: Any?) {
      when (value) {
        is Map<*, *> -> value.values.forEach { visitLeavesImpl(it) }
        is List<*> -> value.forEach { visitLeavesImpl(it) }
        else -> action(value)
      }
    }
    visitLeavesImpl(value)
  }

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
    private val ofZeroOffset = Expression<FloatOffsetValue>(Offset.Zero)
    private val ofZeroPadding = Expression<DpPaddingValue>(ZeroPadding)

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

    fun ofList(list: List<*>): Expression<ListValue<*>> =
      if (list.isEmpty()) ofEmptyList else Expression(list)

    fun ofOffset(offset: Offset) = if (offset == Offset.Zero) ofZeroOffset else Expression(offset)

    fun ofPadding(padding: PaddingValues.Absolute) =
      if (padding == ZeroPadding) ofZeroPadding else Expression(padding)

    fun ofBitmap(value: ImageBitmap): Expression<ImageValue> = Expression(listOf("image", value))
  }
}
