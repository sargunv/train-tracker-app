package dev.sargunv.maplibrecompose.core.source

/**
 * A map data source of tiled vector data.
 *
 * @param id Unique identifier for this source
 * @param uri URI pointing to a JSON file that conforms to the
 *   [TileJSON specification](https://github.com/mapbox/tilejson-spec/)
 */
public expect class VectorSource(id: String, uri: String) : Source
