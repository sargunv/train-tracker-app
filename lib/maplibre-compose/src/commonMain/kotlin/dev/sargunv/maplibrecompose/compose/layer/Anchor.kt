package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

internal val LocalAnchor: ProvidableCompositionLocal<Anchor> = compositionLocalOf { Anchor.Top }

/**
 * Declares where the layers should be anchored, i.e. positioned in the list of layers in the map
 * style.
 *
 * This allows for layers declared in Compose to be inserted at any location of the layers defined
 * in the base style JSON rather than exclusively on top of these.
 *
 * See [AnchorTop], [AnchorBottom], [AnchorAbove], [AnchorBelow], [AnchorReplace] and [AnchorAt] to
 * use in the layers composition.
 */
@Immutable
public sealed interface Anchor {
  /** Layer(s) are anchored at the top, i.e. in front of all other layers */
  public data object Top : Anchor

  /** Layer(s) are anchored at the bottom, i.e. in behind of all other layers */
  public data object Bottom : Anchor

  /** Layer(s) are anchored above the layer with the given [layerId], i.e. in front of it. */
  public data class Above(val layerId: String) : Anchor

  /** Layer(s) are anchored below the layer with the given [layerId], i.e. behind it. */
  public data class Below(val layerId: String) : Anchor

  /** Layer(s) replace the layer with the given [layerId], i.e. are shown instead of it. */
  public data class Replace(val layerId: String) : Anchor
}

/** The layers specified in [block] are put at the top, i.e. in front of all other layers. */
@Composable
public fun AnchorTop(block: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAnchor provides Anchor.Top) { block() }
}

/** The layers specified in [block] are put at the bottom, i.e. behind of all other layers. */
@Composable
public fun AnchorBottom(block: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAnchor provides Anchor.Bottom) { block() }
}

/**
 * The layers specified in [block] are put above the layer with the given [layerId], i.e. in front
 * of it.
 */
@Composable
public fun AnchorAbove(layerId: String, block: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAnchor provides Anchor.Above(layerId)) { block() }
}

/**
 * The layers specified in [block] are put below the layer with the given [layerId], i.e. behind it.
 */
@Composable
public fun AnchorBelow(layerId: String, block: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAnchor provides Anchor.Below(layerId)) { block() }
}

/**
 * The layers specified in [block] replace the layer with the given [layerId], i.e. are shown
 * instead of it.
 */
@Composable
public fun AnchorReplace(layerId: String, block: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAnchor provides Anchor.Replace(layerId)) { block() }
}

/** The layers specified in [block] are put at the given [Anchor]. */
@Composable
public fun AnchorAt(anchor: Anchor, block: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAnchor provides anchor) { block() }
}
