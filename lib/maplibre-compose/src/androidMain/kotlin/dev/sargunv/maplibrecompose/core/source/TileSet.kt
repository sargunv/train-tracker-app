package dev.sargunv.maplibrecompose.core.source

import org.maplibre.android.geometry.LatLng
import org.maplibre.android.style.sources.TileSet as MLNTileSet


internal fun TileSet.toMLNTileSet(): MLNTileSet =
  MLNTileSet(tilejson = tileJsonVersion, tiles = tiles.toTypedArray()).also {
    it.scheme = scheme.value
    it.minZoom = minZoom?.toFloat()
    it.maxZoom = maxZoom?.toFloat()
    it.name = name
    it.version = version
    it.description = description
    it.attribution = attribution
    it.legend = legend
    if (bounds != null) {
      it.setBounds(
        left = bounds.southwest.longitude.toFloat(),
        bottom = bounds.southwest.latitude.toFloat(),
        right = bounds.northeast.longitude.toFloat(),
        top = bounds.northeast.latitude.toFloat()
      )
    }
    if (center != null) {
      it.setCenter(LatLng(latitude = center.latitude, longitude = center.longitude))
    }
  }
