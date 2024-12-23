package dev.sargunv.maplibrecompose.core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Density

internal expect class Image {
  val id: String

  @Suppress("ConvertSecondaryConstructorToPrimary")
  internal constructor(id: String, image: ImageBitmap, density: Density)
}
