package dev.sargunv.maplibrekmp.core

import dev.sargunv.maplibrekmp.core.camera.CameraPosition
import dev.sargunv.maplibrekmp.core.data.GestureSettings
import dev.sargunv.maplibrekmp.core.data.OrnamentSettings
import dev.sargunv.maplibrekmp.core.data.XY
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration

internal interface MaplibreMap {
  var styleUrl: String
  var isDebugEnabled: Boolean
  var cameraPosition: CameraPosition

  fun setOrnamentSettings(value: OrnamentSettings)

  fun setGestureSettings(value: GestureSettings)

  suspend fun animateCameraPosition(finalPosition: CameraPosition, duration: Duration)

  fun positionFromScreenLocation(xy: XY): Position

  fun screenLocationFromPosition(position: Position): XY

  fun queryRenderedFeatures(xy: XY, layerIds: Set<String>): List<Feature>

  interface Callbacks {
    fun onStyleChanged(map: MaplibreMap, style: Style?)

    fun onCameraMove(map: MaplibreMap)

    fun onClick(map: MaplibreMap, latLng: Position, xy: XY)

    fun onLongClick(map: MaplibreMap, latLng: Position, xy: XY)
  }
}
