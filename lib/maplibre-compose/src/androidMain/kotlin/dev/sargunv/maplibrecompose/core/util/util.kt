package dev.sargunv.maplibrecompose.core.util

import android.graphics.PointF
import android.graphics.RectF
import android.view.Gravity
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.Insets
import dev.sargunv.maplibrecompose.core.expression.Point
import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Position
import java.net.URI
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.style.expressions.Expression as MLNExpression

internal fun String.correctedAndroidUri(): URI {
  val uri = URI(this)
  return if (uri.scheme == "file" && uri.path.startsWith("/android_asset/")) {
    URI("asset://${uri.path.removePrefix("/android_asset/")}")
  } else {
    uri
  }
}

internal fun DpOffset.toPointF(density: Density): PointF =
  with(density) { PointF(x.toPx(), y.toPx()) }

internal fun PointF.toOffset(density: Density): DpOffset =
  with(density) { DpOffset(x = x.toDp(), y = y.toDp()) }

internal fun DpRect.toRectF(density: Density): RectF =
  with(density) { RectF(left.toPx(), top.toPx(), right.toPx(), bottom.toPx()) }

internal fun RectF.toRect(density: Density): DpRect =
  with(density) {
    DpRect(left = left.toDp(), top = top.toDp(), right = right.toDp(), bottom = bottom.toDp())
  }

internal fun LatLng.toPosition(): Position = Position(longitude = longitude, latitude = latitude)

internal fun Position.toLatLng(): LatLng = LatLng(latitude = latitude, longitude = longitude)

internal fun LatLngBounds.toBoundingBox(): BoundingBox =
  BoundingBox(northeast = northEast.toPosition(), southwest = southWest.toPosition())

internal fun Expression<*>.toMLNExpression(): MLNExpression? =
  when (value) {
    null -> null
    else -> MLNExpression.Converter.convert(normalizeJsonLike(value))
  }

private fun normalizeJsonLike(value: Any?): JsonElement =
  when (value) {
    null -> JsonNull.INSTANCE
    is Boolean -> JsonPrimitive(value)
    is Number -> JsonPrimitive(value)
    is String -> JsonPrimitive(value)
    is List<*> -> JsonArray().apply { value.forEach { add(normalizeJsonLike(it)) } }
    is Map<*, *> ->
      JsonObject().apply { value.forEach { add(it.key as String, normalizeJsonLike(it.value)) } }

    is Point ->
      JsonArray().apply {
        add("literal")
        add(
          JsonArray().apply {
            add(value.x)
            add(value.y)
          }
        )
      }

    is Insets ->
      JsonArray().apply {
        add("literal")
        add(
          JsonArray().apply {
            add(value.top)
            add(value.right)
            add(value.bottom)
            add(value.left)
          }
        )
      }

    is Color ->
      JsonPrimitive(
        value.toArgb().let {
          "rgba(${(it shr 16) and 0xFF}, ${(it shr 8) and 0xFF}, ${it and 0xFF}, ${value.alpha})"
        }
      )

    else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
  }

internal fun Alignment.toGravity(layoutDir: LayoutDirection): Int {
  val (x, y) = align(IntSize(1, 1), IntSize(3, 3), layoutDir)
  val h =
    when (x) {
      0 -> Gravity.LEFT
      1 -> Gravity.CENTER_HORIZONTAL
      2 -> Gravity.RIGHT
      else -> error("Invalid alignment")
    }
  val v =
    when (y) {
      0 -> Gravity.TOP
      1 -> Gravity.CENTER_VERTICAL
      2 -> Gravity.BOTTOM
      else -> error("Invalid alignment")
    }
  return h or v
}
