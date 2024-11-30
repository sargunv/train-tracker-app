package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// would make this an inline value class, but we lose varargs
// https://youtrack.jetbrains.com/issue/KT-33565/Allow-vararg-parameter-of-inline-class-type
@Immutable
public data class Expression<out T> private constructor(internal val value: Any?) {
  public companion object : ExpressionScope {
    // instantiate some commonly used values so we're not allocating them over and over
    private val constSmallInts = Array<Expression<Number>>(256) { Expression(it) }
    private val constBlack: Expression<Color> = Expression(Color.Black)
    private val constWhite: Expression<Color> = Expression(Color.White)
    private val constTransparent: Expression<Color> = Expression(Color.Transparent)
    private val constFalse: Expression<Boolean> = Expression(false)
    private val constTrue: Expression<Boolean> = Expression(true)
    private val constNull: Expression<Nothing?> = Expression(null)

    // TODO for values not covered by the above, try an LRU cache

    internal fun ofString(string: String): Expression<String> = Expression(string)

    internal fun ofNumber(number: Int): Expression<Number> =
      if (number < constSmallInts.size && number >= 0) constSmallInts[number]
      else Expression(number)

    internal fun ofNumber(number: Float): Expression<Number> = Expression(number)

    internal fun ofColor(color: Color): Expression<Color> =
      when (color) {
        Color.Transparent -> constTransparent
        Color.Black -> constBlack
        Color.White -> constWhite
        else -> Expression(color)
      }

    internal fun ofBoolean(bool: Boolean): Expression<Boolean> = if (bool) constTrue else constFalse

    internal fun ofNull(): Expression<Nothing?> = constNull

    internal fun ofPoint(point: Point): Expression<Point> = Expression(point)

    @Suppress("UNCHECKED_CAST")
    internal fun <T : LayerPropertyEnum> ofLayerPropertyEnum(enum: T): Expression<T> =
      enum.expr as Expression<T>

    internal fun ofInsets(insets: Insets): Expression<Insets> = Expression(insets)

    // return Expression<*> because without ["literal" ... ] MapLibre may not treat it as a list
    internal fun ofList(list: List<Expression<*>>): Expression<*> =
      Expression<Any?>(list.map { it.value })

    // return Expression<*> because without ["literal" ... ] MapLibre may not treat it as a list
    internal fun ofMap(map: Map<String, Expression<*>>): Expression<*> =
      Expression<Any?>(map.entries.associate { (key, value) -> key to value.value })
  }
}
