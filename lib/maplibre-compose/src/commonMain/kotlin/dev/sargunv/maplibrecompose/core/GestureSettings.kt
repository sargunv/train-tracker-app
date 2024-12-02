package dev.sargunv.maplibrecompose.core

import androidx.compose.runtime.Immutable

/**
 * Defines which user map gestures are enabled
 *
 * @param isTiltGesturesEnabled whether user may tilt (pitch) the map by vertically dragging two
 *   fingers up or down
 * @param isZoomGesturesEnabled whether user may zoom the map in and out by pinching two fingers or
 *   by double tapping, holding, and moving the finger up and down as well as simply double tapping
 *   to zoom in
 * @param isRotateGesturesEnabled whether user may rotate the map by moving two fingers in a
 *   circular motion
 * @param isScrollGesturesEnabled whether user may scroll the map by dragging or swiping with one
 *   finger
 */
@Immutable
public data class GestureSettings(
  val isTiltGesturesEnabled: Boolean = true,
  val isZoomGesturesEnabled: Boolean = true,
  val isRotateGesturesEnabled: Boolean = true,
  val isScrollGesturesEnabled: Boolean = true,
) {
  public companion object {
    public val AllEnabled: GestureSettings = GestureSettings()
  }
}
