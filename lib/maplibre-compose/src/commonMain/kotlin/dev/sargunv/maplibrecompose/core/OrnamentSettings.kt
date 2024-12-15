package dev.sargunv.maplibrecompose.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

/**
 * Defines which additional UI elements are displayed on top of the map.
 *
 * @param padding padding of the ornaments to the edge of the map,
 * @param isLogoEnabled whether to display the MapLibre logo.
 * @param logoAlignment where to place the MapLibre logo.
 *
 * On Android, the four corners are supported.
 *
 * On iOS, the four corners, centers along the edges, and the center are supported.
 *
 * @param isAttributionEnabled whether to display copyright attribution information.
 *
 * If `true`, all attribution for all sources used is displayed. If there is not enough space to
 * conveniently display those, a small button in the shape of an ⓘ will be displayed instead.
 * Tapping on it opens the attribution information in a dialog.
 *
 * @param attributionAlignment where to place the attribution information.
 *
 * On Android, the four corners are supported.
 *
 * On iOS, the four corners, centers along the edges, and the center are supported.
 *
 * @param isCompassEnabled whether to display a compass that shows which direction is north. Tapping
 *   on the compass will reset the camera bearing to zero.
 * @param compassAlignment where to place the compass (Android: four corners, iOS: nine positions).
 * @param isScaleBarEnabled whether to display a scale bar that shows the scale of the map.
 * @param scaleBarAlignment where to place the scale bar.
 *
 * On Android, any alignment is supported, but the bar always grows right, so looks best when
 * aligned to the left.
 *
 * On iOS, the four corners, centers along the edges, and the center are supported. The bar grows
 * according to the alignment, so looks best on either side.
 */
@Stable
public data class OrnamentSettings(
  val padding: PaddingValues = PaddingValues(0.dp),
  val isLogoEnabled: Boolean = true,
  val logoAlignment: Alignment = Alignment.BottomStart,
  val isAttributionEnabled: Boolean = true,
  val attributionAlignment: Alignment = Alignment.BottomEnd,
  val isCompassEnabled: Boolean = true,
  val compassAlignment: Alignment = Alignment.TopEnd,
  val isScaleBarEnabled: Boolean = true,
  val scaleBarAlignment: Alignment = Alignment.TopStart,
) {
  public companion object {
    public val AllEnabled: OrnamentSettings = OrnamentSettings()
    public val AttributionOnly: OrnamentSettings =
      OrnamentSettings(isCompassEnabled = false, isScaleBarEnabled = false)
  }
}
