package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.core.layer.LayerPropertyEnum

// would make this an inline value class, but we lose varargs
// https://youtrack.jetbrains.com/issue/KT-33565/Allow-vararg-parameter-of-inline-class-type
@Immutable
public data class Expression<out T> private constructor(internal val value: Any?) {
  public companion object : ExpressionScope {
    internal fun ofString(string: String): Expression<String> = Expression(string)

    internal fun ofNumber(number: Number): Expression<Number> = Expression(number)

    internal fun ofBoolean(bool: Boolean): Expression<Boolean> = Expression(bool)

    internal fun ofNull(): Expression<Nothing?> = Expression(null)

    internal fun ofColor(color: Color): Expression<Color> = Expression(color)

    internal fun ofPoint(point: Point): Expression<Point> = Expression(point)

    internal fun <T : LayerPropertyEnum> ofLayerPropertyEnum(value: T): Expression<T> =
      Expression(value)

    internal fun ofInsets(insets: Insets): Expression<Insets> = Expression(insets)

    // return Expression<*> because without ["literal" ... ] MapLibre may not treat it as a list
    internal fun ofList(list: List<Expression<*>>): Expression<*> =
      Expression<Any?>(list.map { it.value })

    // return Expression<*> because without ["literal" ... ] MapLibre may not treat it as a list
    internal fun ofMap(map: Map<String, Expression<*>>): Expression<*> =
      Expression<Any?>(map.entries.associate { (key, value) -> key to value.value })
  }
}
