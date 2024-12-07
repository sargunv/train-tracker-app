package dev.sargunv.maplibrecompose.core.source

import dev.sargunv.maplibrecompose.core.util.correctedAndroidUri
import org.maplibre.android.style.sources.RasterSource

public actual class RasterSource actual constructor(id: String, configUrl: String, tileSize: Int) :
  Source() {
  override val impl: RasterSource =
    RasterSource(id, configUrl.correctedAndroidUri().toString(), tileSize)
}
