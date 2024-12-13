package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

internal object Literals {
  internal val Null = Expression.Impl(null)

  internal data object True : Expression.Boolean by Expression.Impl(true)

  internal data object False : Expression.Boolean by Expression.Impl(false)

  internal class Float private constructor(number: kotlin.Float) :
    Expression.Float by Expression.Impl(number) {
    companion object {
      private val cached = SmallFloatCache(::Float)

      operator fun invoke(float: kotlin.Float) = cached.get(float) ?: Float(float)
    }
  }

  internal class Int private constructor(number: kotlin.Int) :
    Expression.Int by Expression.Impl(number) {
    companion object {
      private val cached = SmallIntCache(::Int)

      operator fun invoke(int: kotlin.Int) = cached.get(int) ?: Int(int)
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
      private val cached = SmallFloatCache { Dp(it.dp) }

      operator fun invoke(dp: androidx.compose.ui.unit.Dp) = cached.get(dp.value) ?: Dp(dp)
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

  private class SmallFloatCache<out T : Expression>(private val factory: (kotlin.Float) -> T) {
    private val constSmallInts = List(SIZE) { factory(it.toFloat()) }
    private val constSmallFloats = List(SIZE) { factory(it.toFloat() / RESOLUTION) }

    private fun isSmallInt(f: kotlin.Float) = f >= 0 && f < SIZE && f.toInt().toFloat() == f

    fun get(float: kotlin.Float) =
      when {
        isSmallInt(float) -> constSmallInts[float.toInt()]
        isSmallInt(float * RESOLUTION) -> constSmallFloats[(float * 20f).toInt()]
        else -> null
      }

    companion object {
      private const val SIZE = 512
      private const val RESOLUTION = 20f
    }
  }

  private class SmallIntCache<out T : Expression>(private val factory: (kotlin.Int) -> T) {
    private val constSmallInts = List(SIZE) { factory(it) }

    fun get(int: kotlin.Int) =
      when {
        int in 0..<SIZE -> constSmallInts[int]
        else -> null
      }

    companion object {
      private const val SIZE = 512
    }
  }
}
