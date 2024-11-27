package dev.sargunv.maplibrecompose.compose

import androidx.compose.ui.geometry.Offset
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position

/**
 * A callback for when the map is clicked. Called before any layer click handlers.
 *
 * @return [ClickResult.Consume] if this click should be consumed and not passed down to layers or
 *   [ClickResult.Pass] if it should be passed down.
 */
public typealias MapClickHandler = (Position, Offset) -> ClickResult

/**
 * A callback for when a feature is clicked.
 *
 * @return [ClickResult.Consume] if this click should be consumed and not passed down to layers
 *   rendered below this one or [ClickResult.Pass] if it should be passed down.
 */
public typealias FeaturesClickHandler = (List<Feature>) -> ClickResult

/** The result of a click event handler. See [MapClickHandler] and [FeaturesClickHandler]. */
public enum class ClickResult(internal val consumed: Boolean) {
  Consume(true),
  Pass(false),
}
