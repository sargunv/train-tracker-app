package dev.sargunv.maplibrecompose.compose.engine

import androidx.compose.ui.graphics.ImageBitmap

internal class ImageManager(private val styleManager: StyleManager) {
  private val idMap = IncrementingIdMap<ImageBitmap>("image")

  private val counter =
    ReferenceCounter<ImageBitmap>(
      onZeroToOne = { image ->
        val id = idMap.addId(image)
        styleManager.logger?.i { "Adding image $id" }
        styleManager.style.addImage(id, image)
      },
      onOneToZero = { image ->
        val id = idMap.removeId(image)
        styleManager.logger?.i { "Removing image $id" }
        styleManager.style.removeImage(id)
      },
    )

  fun addReference(image: ImageBitmap): String {
    counter.increment(image)
    return idMap.getId(image)
  }

  fun removeReference(image: ImageBitmap) {
    counter.decrement(image)
  }
}
