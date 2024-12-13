package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

internal class ExpressionImpl private constructor(override val value: Any?) :
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
