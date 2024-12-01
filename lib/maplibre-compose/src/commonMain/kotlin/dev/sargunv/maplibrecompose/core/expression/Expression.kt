package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

// would make this an inline value class, but we lose varargs
// https://youtrack.jetbrains.com/issue/KT-33565/Allow-vararg-parameter-of-inline-class-type
@Immutable
public data class Expression<out T> private constructor(internal val value: Any?) {
  public companion object : ExpressionScope {
    // instantiate some commonly used values so we're not allocating them over and over
    private val constSmallInts = Array<Expression<Number>>(512) { Expression(it) }
    private val constSmallFloats = Array<Expression<Number>>(512) { Expression(it.toFloat() / 20f) }
    private val constBlack: Expression<Color> = Expression(Color.Black)
    private val constWhite: Expression<Color> = Expression(Color.White)
    private val constTransparent: Expression<Color> = Expression(Color.Transparent)
    private val constFalse: Expression<Boolean> = Expression(false)
    private val constTrue: Expression<Boolean> = Expression(true)
    private val constNull: Expression<Nothing?> = Expression(null)
    private val constZeroOffset: Expression<Offset> = Expression(Offset.Zero)
    private val constZeroPadding: Expression<PaddingValues.Absolute> = Expression(ZeroPadding)

    // TODO for values not covered by the above, try an LRU cache

    internal fun ofString(string: String): Expression<String> = Expression(string)

    private fun Float.isSmallInt(): Boolean =
      this >= 0 && this < constSmallInts.size && this.toInt().toFloat() == this

    internal fun ofFloat(float: Float): Expression<Number> {
      return when {
        float.isSmallInt() -> constSmallInts[float.toInt()]
        (float * 20f).isSmallInt() -> constSmallFloats[(float * 20f).toInt()]
        else -> Expression(float)
      }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun ofDp(dp: Dp): Expression<Dp> = ofFloat(dp.value) as Expression<Dp>

    internal fun ofColor(color: Color): Expression<Color> =
      when (color) {
        Color.Transparent -> constTransparent
        Color.Black -> constBlack
        Color.White -> constWhite
        else -> Expression(color)
      }

    internal fun ofBoolean(bool: Boolean): Expression<Boolean> = if (bool) constTrue else constFalse

    internal fun ofNull(): Expression<Nothing?> = constNull

    internal fun ofOffset(offset: Offset): Expression<Offset> =
      if (offset == Offset.Zero) constZeroOffset else Expression(offset)

    @Suppress("UNCHECKED_CAST")
    internal fun ofDpOffset(dpOffset: DpOffset): Expression<DpOffset> =
      (if (dpOffset == DpOffset.Zero) constZeroOffset
      else Expression(Offset(dpOffset.x.value, dpOffset.y.value)))
        as Expression<DpOffset>

    @Suppress("UNCHECKED_CAST")
    internal fun <T : LayerPropertyEnum> ofLayerPropertyEnum(enum: T): Expression<T> =
      enum.expr as Expression<T>

    internal fun ofPadding(padding: PaddingValues.Absolute): Expression<PaddingValues.Absolute> =
      if (padding == ZeroPadding) constZeroPadding else Expression(padding)

    // return Expression<*> because without ["literal" ... ] MapLibre may not treat it as a list
    internal fun ofList(list: List<Expression<*>>): Expression<*> =
      Expression<Any?>(list.map { it.value })

    // return Expression<*> because without ["literal" ... ] MapLibre may not treat it as a list
    internal fun ofMap(map: Map<String, Expression<*>>): Expression<*> =
      Expression<Any?>(map.entries.associate { (key, value) -> key to value.value })
  }
}
