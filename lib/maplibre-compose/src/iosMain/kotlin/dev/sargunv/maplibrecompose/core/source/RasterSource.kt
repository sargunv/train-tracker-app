package dev.sargunv.maplibrecompose.core.source

import cocoapods.MapLibre.MLNRasterTileSource
import platform.Foundation.NSURL

public actual class RasterSource actual constructor(id: String, uri: String, tileSize: Int) :
  Source() {
  override val impl: MLNRasterTileSource =
    MLNRasterTileSource(id, NSURL(string = uri), tileSize.toDouble())
}
