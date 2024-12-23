package dev.sargunv.maplibrecompose.core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Density
import dev.sargunv.maplibrecompose.core.util.toUIImage
import platform.UIKit.UIImage

internal actual class Image(actual val id: String, val impl: UIImage) {
  internal actual constructor(
    id: String,
    image: ImageBitmap,
    density: Density,
  ) : this(id, image.toUIImage(density))
}
