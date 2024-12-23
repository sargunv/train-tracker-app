package dev.sargunv.maplibrecompose.core

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.Density

internal actual class Image(actual val id: String, val impl: Bitmap) {
  internal actual constructor(
    id: String,
    image: ImageBitmap,
    density: Density,
  ) : this(id, image.asAndroidBitmap())
}
