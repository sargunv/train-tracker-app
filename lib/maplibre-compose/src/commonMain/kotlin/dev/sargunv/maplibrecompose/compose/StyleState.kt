package dev.sargunv.maplibrecompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.sargunv.maplibrecompose.core.Style
import dev.sargunv.maplibrecompose.core.source.AttributionLink

@Composable
public fun rememberStyleState(): StyleState {
  return remember { StyleState() }
}

public class StyleState internal constructor() {
  private var style: Style? = null

  internal fun attach(style: Style?) {
    if (this.style != style) {
      this.style = style
    }
  }

  public fun queryAttributionLinks(): List<AttributionLink> {
    // TODO expose this as State somehow?
    return style?.getSources()?.flatMap { it.attributionLinks } ?: emptyList()
  }
}
