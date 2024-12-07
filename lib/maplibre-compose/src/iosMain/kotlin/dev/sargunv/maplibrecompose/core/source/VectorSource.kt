package dev.sargunv.maplibrecompose.core.source

import cocoapods.MapLibre.MLNVectorTileSource
import platform.Foundation.NSURL

public actual class VectorSource actual constructor(id: String, uri: String) : Source() {
  override val impl: MLNVectorTileSource = MLNVectorTileSource(id, NSURL(string = uri))
}
