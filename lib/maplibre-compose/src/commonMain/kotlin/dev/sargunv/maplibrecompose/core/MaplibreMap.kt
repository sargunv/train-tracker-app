package dev.sargunv.maplibrecompose.core

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import dev.sargunv.maplibrecompose.core.camera.CameraPosition
import dev.sargunv.maplibrecompose.core.data.GestureSettings
import dev.sargunv.maplibrecompose.core.data.OrnamentSettings
import dev.sargunv.maplibrecompose.core.expression.Expression
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration

internal interface MaplibreMap {
  var styleUrl: String
  var isDebugEnabled: Boolean
  var cameraPosition: CameraPosition

  fun setMaximumFps(maximumFps: Int)

  fun setOrnamentSettings(value: OrnamentSettings)

  fun setGestureSettings(value: GestureSettings)

  suspend fun animateCameraPosition(finalPosition: CameraPosition, duration: Duration)

  fun positionFromScreenLocation(offset: Offset): Position

  fun screenLocationFromPosition(position: Position): Offset

  fun queryRenderedFeatures(offset: Offset): List<Feature>

  fun queryRenderedFeatures(offset: Offset, layerIds: Set<String>): List<Feature>

  fun queryRenderedFeatures(
    offset: Offset,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature>

  fun queryRenderedFeatures(rect: Rect): List<Feature>

  fun queryRenderedFeatures(rect: Rect, layerIds: Set<String>): List<Feature>

  fun queryRenderedFeatures(
    rect: Rect,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature>

  interface Callbacks {
    fun onStyleChanged(map: MaplibreMap, style: Style?)

    fun onCameraMove(map: MaplibreMap)

    fun onClick(map: MaplibreMap, latLng: Position, offset: Offset)

    fun onLongClick(map: MaplibreMap, latLng: Position, offset: Offset)
  }
}
