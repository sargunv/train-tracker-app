package dev.sargunv.maplibrekmp.core.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import cocoapods.MapLibre.MLNFeatureProtocol
import cocoapods.MapLibre.MLNShape
import cocoapods.MapLibre.expressionWithMLNJSONObject
import cocoapods.MapLibre.predicateWithMLNJSONObject
import dev.sargunv.maplibrekmp.core.data.XY
import dev.sargunv.maplibrekmp.expression.Expression
import dev.sargunv.maplibrekmp.expression.Point
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.GeoJson
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.cinterop.CValue
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGSizeMake
import platform.CoreGraphics.CGVectorMake
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSData
import platform.Foundation.NSExpression
import platform.Foundation.NSPredicate
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSValue
import platform.Foundation.dataWithBytes
import platform.UIKit.UIColor
import platform.UIKit.valueWithCGVector

internal fun ByteArray.toNSData(): NSData {
  return usePinned { NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong()) }
}

internal fun MLNFeatureProtocol.toFeature(): Feature {
  return Feature.fromJson(JsonElement.convert(geoJSONDictionary()) as JsonObject)
}

internal fun JsonElement.Companion.convert(any: Any?): JsonElement {
  return when (any) {
    null -> JsonNull
    is Boolean -> JsonPrimitive(any)
    is Number -> JsonPrimitive(any)
    is String -> JsonPrimitive(any)
    is List<*> -> JsonArray(any.map { convert(it) })
    is Map<*, *> -> JsonObject(any.entries.associate { (k, v) -> k as String to convert(v) })
    else -> error("Unsupported type: ${any::class.simpleName}")
  }
}

internal fun CValue<CGPoint>.toXY(): XY = useContents { XY(x = x.toFloat(), y = y.toFloat()) }

internal fun XY.toCGPoint(): CValue<CGPoint> = CGPointMake(x = x.toDouble(), y = y.toDouble())

internal fun CValue<CLLocationCoordinate2D>.toPosition(): Position = useContents {
  Position(longitude = longitude, latitude = latitude)
}

internal fun Position.toCLLocationCoordinate2D(): CValue<CLLocationCoordinate2D> =
  CLLocationCoordinate2DMake(latitude = latitude, longitude = longitude)

internal fun DpSize.toCGSize() =
  CGSizeMake(width = width.value.toDouble(), height = height.value.toDouble())

internal fun GeoJson.toMLNShape(): MLNShape {
  return MLNShape.shapeWithData(
    data = json().encodeToByteArray().toNSData(),
    encoding = NSUTF8StringEncoding,
    error = null,
  )!!
}


internal fun Expression<*>.toNSExpression(): NSExpression =
  when (value) {
    null -> NSExpression.expressionForConstantValue(null)
    else -> NSExpression.expressionWithMLNJSONObject(normalizeJsonLike(value)!!)
  }

internal fun Expression<Boolean>.toPredicate(): NSPredicate? =
  value?.let { NSPredicate.predicateWithMLNJSONObject(normalizeJsonLike(it)!!) }

private fun normalizeJsonLike(value: Any?): Any? =
  when (value) {
    null -> null
    is Boolean -> value
    is Number -> value
    is String -> value
    is List<*> -> value.map(::normalizeJsonLike)
    is Map<*, *> -> value.mapValues { normalizeJsonLike(it.value) }
    is Point -> NSValue.valueWithCGVector(CGVectorMake(value.x.toDouble(), value.y.toDouble()))
    is Color ->
      UIColor.colorWithRed(
        red = value.red.toDouble(),
        green = value.green.toDouble(),
        blue = value.blue.toDouble(),
        alpha = value.alpha.toDouble(),
      )
    else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
  }