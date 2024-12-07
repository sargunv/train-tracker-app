package dev.sargunv.maplibrecompose.core.source

import io.github.dellisd.spatialk.geojson.GeoJson

public expect class GeoJsonSource : Source {
  public constructor(id: String, uri: String, options: GeoJsonOptions)

  public constructor(id: String, data: GeoJson, options: GeoJsonOptions)

  public fun setUri(uri: String)

  public fun setData(geoJson: GeoJson)
}
