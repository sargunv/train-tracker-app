package dev.sargunv.maplibrecompose.core.source

import io.github.dellisd.spatialk.geojson.GeoJson

/**
 *  A map data source consisting of geojson data.
 * */
public expect class GeoJsonSource : Source {
  /**
   *  @param id Unique identifier for this source
   *  @param dataUrl URI pointing to a GeoJson file
   *  @param options see [GeoJsonOptions]
   */
  public constructor(id: String, dataUrl: String, options: GeoJsonOptions)

  /**
   *  @param id Unique identifier for this source
   *  @param data GeoJson data
   *  @param options see [GeoJsonOptions]
   */
  public constructor(id: String, data: GeoJson, options: GeoJsonOptions)

  public fun setDataUrl(url: String)

  public fun setData(geoJson: GeoJson)
}
