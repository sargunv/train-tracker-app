package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

internal object Literals {
  internal val Null = Expression.Impl(null)

  internal data object True : Expression.Boolean by Expression.Impl(true)

  internal data object False : Expression.Boolean by Expression.Impl(false)

  internal class Number private constructor(number: kotlin.Number) :
    Expression.Number by Expression.Impl(number) {
    companion object {
      private val NumberCache = NumberCache(::Number)

      operator fun invoke(float: Float) = NumberCache.get(float)
    }
  }

  internal class String private constructor(string: kotlin.String) :
    Expression.String by Expression.Impl(string) {
    companion object {
      private val empty = String("")

      operator fun invoke(string: kotlin.String) = if (string.isEmpty()) empty else String(string)
    }
  }

  internal class Color private constructor(color: androidx.compose.ui.graphics.Color) :
    Expression.Color by Expression.Impl(color) {
    companion object {
      private val transparent = Color(androidx.compose.ui.graphics.Color.Transparent)
      private val black = Color(androidx.compose.ui.graphics.Color.Black)
      private val white = Color(androidx.compose.ui.graphics.Color.White)

      operator fun invoke(color: androidx.compose.ui.graphics.Color) =
        when (color) {
          androidx.compose.ui.graphics.Color.Transparent -> transparent
          androidx.compose.ui.graphics.Color.Black -> black
          androidx.compose.ui.graphics.Color.White -> white
          else -> Color(color)
        }
    }
  }

  internal class Dp private constructor(dp: androidx.compose.ui.unit.Dp) :
    Expression.Dp by Expression.Impl(dp.value) {
    companion object {
      private val NumberCache = NumberCache { Dp(it.dp) }

      operator fun invoke(dp: androidx.compose.ui.unit.Dp) = NumberCache.get(dp.value)
    }
  }

  internal class Offset private constructor(offset: androidx.compose.ui.geometry.Offset) :
    Expression.Offset by Expression.Impl(offset) {
    companion object {
      private val Zero = Offset(androidx.compose.ui.geometry.Offset.Zero)

      operator fun invoke(offset: androidx.compose.ui.geometry.Offset) =
        if (offset == androidx.compose.ui.geometry.Offset.Zero) Zero else Offset(offset)
    }
  }

  internal class DpOffset private constructor(dpOffset: androidx.compose.ui.unit.DpOffset) :
    Expression.DpOffset by Expression.Impl(
      androidx.compose.ui.geometry.Offset(x = dpOffset.x.value, y = dpOffset.y.value)
    ) {

    companion object {
      private val Zero = DpOffset(androidx.compose.ui.unit.DpOffset.Zero)

      operator fun invoke(dpOffset: androidx.compose.ui.unit.DpOffset) =
        if (dpOffset == androidx.compose.ui.unit.DpOffset.Zero) Zero else DpOffset(dpOffset)
    }
  }

  internal class Padding private constructor(padding: PaddingValues.Absolute) :
    Expression.Padding by Expression.Impl(padding) {
    companion object {
      private val Zero = Padding(ZeroPadding)

      operator fun invoke(padding: PaddingValues.Absolute) =
        if (padding == PaddingValues.Absolute(0.dp)) Zero else Padding(padding)
    }
  }

  private class NumberCache<out T : Expression>(private val factory: (Float) -> T) {
    private val constSmallInts = List(SIZE) { factory(it.toFloat()) }
    private val constSmallFloats = List(SIZE) { factory(it.toFloat() / RESOLUTION) }

    private fun isSmallInt(f: Float) = f >= 0 && f < SIZE && f.toInt().toFloat() == f

    fun get(float: Float) =
      when {
        isSmallInt(float) -> constSmallInts[float.toInt()]
        isSmallInt(float * RESOLUTION) -> constSmallFloats[(float * 20f).toInt()]
        else -> factory(float)
      }

    companion object {
      private const val SIZE = 512
      private const val RESOLUTION = 20f
    }
  }
}
