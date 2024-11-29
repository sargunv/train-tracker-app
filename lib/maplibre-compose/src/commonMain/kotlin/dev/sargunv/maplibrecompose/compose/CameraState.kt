package dev.sargunv.maplibrecompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import dev.sargunv.maplibrecompose.core.MaplibreMap
import dev.sargunv.maplibrecompose.core.camera.CameraPosition
import dev.sargunv.maplibrecompose.core.expression.Expression
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.channels.Channel

@Composable
public fun rememberCameraState(firstPosition: CameraPosition = CameraPosition()): CameraState {
  return remember { CameraState(firstPosition) }
}

@Stable
public class CameraState internal constructor(firstPosition: CameraPosition) {
  internal var map: MaplibreMap? = null
    set(map) {
      if (map != null && map !== field) {
        map.cameraPosition = position
        mapAttachSignal.trySend(map)
      }
      field = map
    }

  private val mapAttachSignal = Channel<MaplibreMap>()

  internal val positionState = mutableStateOf(firstPosition)

  // if the map is not yet initialized, we store the value to apply it later
  public var position: CameraPosition
    get() = positionState.value
    set(value) {
      map?.cameraPosition = value
      positionState.value = value
    }

  public val isInitialized: Boolean
    get() = map != null

  public suspend fun awaitInitialized() {
    map ?: mapAttachSignal.receive()
  }

  public suspend fun animateTo(
    finalPosition: CameraPosition,
    duration: Duration = 300.milliseconds,
  ) {
    val map = map ?: mapAttachSignal.receive()
    map.animateCameraPosition(finalPosition, duration)
  }

  private fun requireIsInitialized() {
    require(isInitialized) {
      "Map requested before it was initialized; try calling awaitInitialization() first"
    }
  }

  public fun screenLocationFromPosition(position: Position): DpOffset {
    requireIsInitialized()
    return map!!.screenLocationFromPosition(position)
  }

  public fun positionFromScreenLocation(offset: DpOffset): Position {
    requireIsInitialized()
    return map!!.positionFromScreenLocation(offset)
  }

  public fun queryRenderedFeatures(offset: DpOffset): List<Feature> {
    return map?.queryRenderedFeatures(offset) ?: emptyList()
  }

  public fun queryRenderedFeatures(offset: DpOffset, layerIds: Set<String>): List<Feature> {
    return map?.queryRenderedFeatures(offset, layerIds) ?: emptyList()
  }

  public fun queryRenderedFeatures(
    offset: DpOffset,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature> {
    return map?.queryRenderedFeatures(offset, layerIds, predicate) ?: emptyList()
  }

  public fun queryRenderedFeatures(rect: DpRect): List<Feature> {
    return map?.queryRenderedFeatures(rect) ?: emptyList()
  }

  public fun queryRenderedFeatures(rect: DpRect, layerIds: Set<String>): List<Feature> {
    return map?.queryRenderedFeatures(rect, layerIds) ?: emptyList()
  }

  public fun queryRenderedFeatures(
    rect: DpRect,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature> {
    return map?.queryRenderedFeatures(rect, layerIds, predicate) ?: emptyList()
  }
}
