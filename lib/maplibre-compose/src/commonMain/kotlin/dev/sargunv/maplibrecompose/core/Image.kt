package dev.sargunv.maplibrecompose.core

import androidx.compose.ui.graphics.ImageBitmap

internal expect class Image {
  val id: String

  @Suppress("ConvertSecondaryConstructorToPrimary")
  internal constructor(id: String, image: ImageBitmap)
}
