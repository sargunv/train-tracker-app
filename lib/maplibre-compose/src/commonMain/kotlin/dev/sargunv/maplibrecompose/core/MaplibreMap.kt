package dev.sargunv.maplibrecompose.core

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import dev.sargunv.maplibrecompose.core.expression.Expression
import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration

internal interface MaplibreMap {
  var styleUrl: String
  var isDebugEnabled: Boolean
  var cameraPosition: CameraPosition
  var onFpsChanged: (Double) -> Unit

  val visibleBoundingBox: BoundingBox

  fun setMaximumFps(maximumFps: Int)

  fun setOrnamentSettings(value: OrnamentSettings)

  fun setGestureSettings(value: GestureSettings)

  suspend fun animateCameraPosition(finalPosition: CameraPosition, duration: Duration)

  fun positionFromScreenLocation(offset: DpOffset): Position

  fun screenLocationFromPosition(position: Position): DpOffset

  fun queryRenderedFeatures(offset: DpOffset): List<Feature>

  fun queryRenderedFeatures(offset: DpOffset, layerIds: Set<String>): List<Feature>

  fun queryRenderedFeatures(
    offset: DpOffset,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature>

  fun queryRenderedFeatures(rect: DpRect): List<Feature>

  fun queryRenderedFeatures(rect: DpRect, layerIds: Set<String>): List<Feature>

  fun queryRenderedFeatures(
    rect: DpRect,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature>

  interface Callbacks {
    fun onStyleChanged(map: MaplibreMap, style: Style?)

    fun onCameraMove(map: MaplibreMap)

    fun onClick(map: MaplibreMap, latLng: Position, offset: DpOffset)

    fun onLongClick(map: MaplibreMap, latLng: Position, offset: DpOffset)
  }
}
