package dev.sargunv.maplibrecompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import dev.sargunv.maplibrecompose.core.CameraMoveReason
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.MaplibreMap
import dev.sargunv.maplibrecompose.core.VisibleRegion
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.const
import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.channels.Channel

@Composable
public fun rememberCameraState(firstPosition: CameraPosition = CameraPosition()): CameraState {
  return remember { CameraState(firstPosition) }
}

/** Use this class to access information about the map in relation to the camera. */
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
  internal val moveReasonState = mutableStateOf(CameraMoveReason.NONE)

  /** how the camera is oriented towards the map */
  // if the map is not yet initialized, we store the value to apply it later
  public var position: CameraPosition
    get() = positionState.value
    set(value) {
      map?.cameraPosition = value
      positionState.value = value
    }

  /** reason why the camera moved, last time it moved */
  public val moveReason: CameraMoveReason
    get() = moveReasonState.value

  /** suspends until the map has been initialized */
  public suspend fun awaitInitialized() {
    map ?: mapAttachSignal.receive()
  }

  /** Animates the camera towards the [finalPosition] in [duration] time. */
  public suspend fun animateTo(
    finalPosition: CameraPosition,
    duration: Duration = 300.milliseconds,
  ) {
    val map = map ?: mapAttachSignal.receive()
    map.animateCameraPosition(finalPosition, duration)
  }

  private fun requireMap(): MaplibreMap {
    check(map != null) {
      "Map requested before it was initialized; try calling awaitInitialization() first"
    }
    return map!!
  }

  /**
   * Returns an offset from the top-left corner of the map composable that corresponds to the given
   * [position]. This works for positions that are off-screen, too.
   *
   * @throws IllegalStateException if the map is not initialized yet. See [awaitInitialized].
   */
  public fun screenLocationFromPosition(position: Position): DpOffset {
    return requireMap().screenLocationFromPosition(position)
  }

  /**
   * Returns a position that corresponds to the given [offset] from the top-left corner of the map
   * composable.
   *
   * @throws IllegalStateException if the map is not initialized yet. See [awaitInitialized].
   */
  public fun positionFromScreenLocation(offset: DpOffset): Position {
    return requireMap().positionFromScreenLocation(offset)
  }

  /**
   * Returns a list of features that are rendered at the given [offset] from the top-left corner of
   * the map composable, optionally limited to layers with the given [layerIds] and filtered by the
   * given [predicate]. The result is sorted by render order, i.e. the feature in front is first in
   * the list.
   *
   * @param offset position from the top-left corner of the map composable to query for
   * @param layerIds the ids of the layers to limit the query to. If not specified, features in
   *   *any* layer are returned
   * @param predicate expression that has to evaluate to true for a feature to be included in the
   *   result
   */
  public fun queryRenderedFeatures(
    offset: DpOffset,
    layerIds: Set<String>? = null,
    predicate: Expression<Boolean> = const(true),
  ): List<Feature> {
    val predicateOrNull = predicate.takeUnless { it == const(true) }
    return map?.queryRenderedFeatures(offset, layerIds, predicateOrNull) ?: emptyList()
  }

  /**
   * Returns a list of features whose rendered geometry intersect with the given [rect], optionally
   * limited to layers with the given [layerIds] and filtered by the given [predicate]. The result
   * is sorted by render order, i.e. the feature in front is first in the list.
   *
   * @param rect rectangle to intersect with rendered geometry
   * @param layerIds the ids of the layers to limit the query to. If not specified, features in
   *   *any* layer are returned
   * @param predicate expression that has to evaluate to true for a feature to be included in the
   *   result
   */
  public fun queryRenderedFeatures(
    rect: DpRect,
    layerIds: Set<String>? = null,
    predicate: Expression<Boolean> = const(true),
  ): List<Feature> {
    val predicateOrNull = predicate.takeUnless { it == const(true) }
    return map?.queryRenderedFeatures(rect, layerIds, predicateOrNull) ?: emptyList()
  }

  /**
   * Returns the smallest bounding box that contains the currently visible area.
   *
   * Note that the bounding box is always a north-aligned rectangle. I.e. if the map is rotated or
   * tilted, the returned bounding box will always be larger than the actually visible area. See
   * [queryVisibleRegion]
   *
   * @throws IllegalStateException if the map is not initialized yet. See [awaitInitialized].
   */
  public fun queryVisibleBoundingBox(): BoundingBox {
    // TODO at some point, this should be refactored to State, just like the camera position
    return requireMap().visibleBoundingBox
  }

  /**
   * Returns the currently visible area, which is a four-sided polygon spanned by the four points
   * each at one corner of the map composable. If the camera has tilt (pitch), this polygon is a
   * trapezoid instead of a rectangle.
   *
   * @throws IllegalStateException if the map is not initialized yet. See [awaitInitialized].
   */
  public fun queryVisibleRegion(): VisibleRegion {
    // TODO at some point, this should be refactored to State, just like the camera position
    return requireMap().visibleRegion
  }
}
