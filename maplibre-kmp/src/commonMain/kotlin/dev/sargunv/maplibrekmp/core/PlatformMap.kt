package dev.sargunv.maplibrekmp.core

import dev.sargunv.maplibrekmp.core.camera.CameraPosition
import io.github.dellisd.spatialk.geojson.Feature
import kotlin.time.Duration

internal expect class PlatformMap private constructor() {

  var isDebugEnabled: Boolean
  var isLogoEnabled: Boolean
  var isAttributionEnabled: Boolean
  var isCompassEnabled: Boolean
  var isRotateGesturesEnabled: Boolean
  var isScrollGesturesEnabled: Boolean
  var isTiltGesturesEnabled: Boolean
  var isZoomGesturesEnabled: Boolean

  var cameraPosition: CameraPosition

  suspend fun animateCameraPosition(finalPosition: CameraPosition, duration: Duration)

  fun queryRenderedFeatures(xy: Pair<Float, Float>, layerIds: Set<String>): List<Feature>
}
