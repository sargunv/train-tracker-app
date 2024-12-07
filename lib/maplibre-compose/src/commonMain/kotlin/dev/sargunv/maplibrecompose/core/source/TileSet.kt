package dev.sargunv.maplibrecompose.core.source

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Position

/**
 * Metadata for a tile set.
 *
 * See the [TileJson Specification](https://github.com/mapbox/tilejson-spec/)
 *
 * @param tiles A list of tile endpoints. {z}, {x} and {y} in these strings are replaced with the
 *   corresponding integers. If multiple endpoints are specified, any combination of endpoints may
 *   be used. All endpoints MUST return the same content for the same URL. The list MUST contain at
 *   least one endpoint.
 * @param scheme Scheme how the tiles are being accessed, i.e. how the tiles are named
 * @param minZoom Minimum zoom level at which tiles are available in this source. A value in range
 *   `[0..30]`
 * @param maxZoom Maximum zoom level at which tiles are available in this source. A value in range
 *   `[0..30]`, must be greater or equal `minZoom`
 * @param name A name describing this set of tiles.
 * @param version A semantic version number for the tileset.
 * @param description A text description of the tileset.
 * @param attribution Attribution to be displayed when the map is shown to a user. May be styled
 *   with simple HTML.
 * @param legend A legend text. May be styled with simple HTML.
 * @param bounds The region for which the tiles from this tile source are available.
 * @param center Center of this tileset, must be within [bounds].
 *   Implementations may use this value to set the default location.
 * */
public data class TileSet(
  val tileJsonVersion: String,
  val tiles: List<String>,
  // add vectorLayers (only for vector source),
  // data (for what?), fillzoom, grids, template,
  // encoding (only for raster-dem tileset)
  val scheme: TileScheme = TileScheme.XYZ,
  val minZoom: Int? = null,
  val maxZoom: Int? = null,
  val name: String? = null,
  val version: String? = null,
  val description: String? = null,
  val attribution: String? = null,
  val legend: String? = null,
  val bounds: BoundingBox? = null,
  val center: Position? = null,
) {
  init {
    if (minZoom != null) {
      require(minZoom < 0) { "minZoom must be 0 or greater" }
      require(minZoom > 30) { "minZoom must be 30 or smaller" }
    }
    if (maxZoom != null) {
      require(maxZoom < 0) { "minZoom must be 0 or greater" }
      require(maxZoom > 30) { "minZoom must be 30 or smaller" }
    }
    if (minZoom != null && maxZoom != null) {
      require(maxZoom >= minZoom) { "maxZoom must be greater or equal minZoom" }
    }
  }

  internal fun toMap(): Map<String, Any> =
    listOfNotNull(
      "tilejson" to tileJsonVersion,
      "tiles" to tiles,
      "scheme" to scheme.value,
      minZoom?.let { "minzoom" to it },
      maxZoom?.let { "maxzoom" to it },
      name?.let { "name" to it },
      version?.let { "version" to it },
      description?.let { "description" to it },
      attribution?.let { "attribution" to it },
      legend?.let { "legend" to it },
      bounds?.let {
        val sw = it.southwest
        val ne = it.northeast
        "bounds" to doubleArrayOf(sw.longitude, sw.latitude, ne.longitude, ne.latitude)
      },
      center?.let { "center" to doubleArrayOf(it.longitude, it.latitude) }
    ).toMap()
}

/** Defines the scheme how the tiles are being accessed, i.e. how the tiles are named */
public enum class TileScheme(internal val value: String) {
  /** The tiles are accessed via the naming convention for slippy maps. */
  XYZ("xyz"),
  /** The tiles are accessed using the
   *  [Tile Map Service schema](https://wiki.osgeo.org/wiki/Tile_Map_Service_Specification). */
  TMS("tms")
}
