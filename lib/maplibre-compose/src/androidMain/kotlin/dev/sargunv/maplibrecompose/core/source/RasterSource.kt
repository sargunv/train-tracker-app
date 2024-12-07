package dev.sargunv.maplibrecompose.core.source

import dev.sargunv.maplibrecompose.core.util.correctedAndroidUri
import org.maplibre.android.style.sources.RasterSource

public actual class RasterSource actual constructor(id: String, uri: String) : Source() {
  override val impl: RasterSource = RasterSource(id, uri.correctedAndroidUri().toString())
}
