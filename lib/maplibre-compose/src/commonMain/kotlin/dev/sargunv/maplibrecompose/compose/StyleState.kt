package dev.sargunv.maplibrecompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import dev.sargunv.maplibrecompose.core.Image
import dev.sargunv.maplibrecompose.core.Style
import dev.sargunv.maplibrecompose.core.source.AttributionLink

@Composable
public fun rememberStyleState(images: Map<String, ImageBitmap> = emptyMap()): StyleState {
  val ret = remember { StyleState() }
  val density = LocalDensity.current
  remember(images, density) { ret.updateImages(images, density) }
  return ret
}

public class StyleState internal constructor() {
  private var style: Style? = null

  private val addedImages = mutableSetOf<Image>()

  internal fun attach(style: Style?) {
    if (this.style != style) {
      this.style = style
      if (style != null) {
        addedImages.forEach { style.addImage(it) }
      }
    }
  }

  internal fun updateImages(images: Map<String, ImageBitmap>, density: Density) {
    val newImages = images.map { (id, bitmap) -> Image(id, bitmap, density) }.toSet()
    style?.let { style ->
      val toRemove = addedImages - newImages
      val toAdd = newImages - addedImages
      toRemove.forEach { style.removeImage(it) }
      toAdd.forEach { style.addImage(it) }
    }
    addedImages.clear()
    addedImages.addAll(newImages)
  }

  public fun queryAttributionLinks(): List<AttributionLink> {
    // TODO expose this as State somehow?
    return style?.getSources()?.flatMap { it.attributionLinks } ?: emptyList()
  }
}
