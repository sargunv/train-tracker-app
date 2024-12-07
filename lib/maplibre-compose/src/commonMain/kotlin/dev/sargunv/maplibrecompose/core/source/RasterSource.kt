package dev.sargunv.maplibrecompose.core.source

/**
 * A map data source of tiled map pictures.
 *
 * @param id Unique identifier for this source
 * @param uri URI pointing to a JSON file that conforms to the
 *   [TileJSON specification](https://github.com/mapbox/tilejson-spec/)
 * @param tileSize width and height (measured in points) of each tiled image in the raster tile
 *   source
 */
public expect class RasterSource(
  id: String,
  uri: String,
  tileSize: Int = DEFAULT_RASTER_TILE_SIZE,
) : Source

public const val DEFAULT_RASTER_TILE_SIZE: Int = 512
