package dev.sargunv.maplibrecompose.core.source

import androidx.compose.runtime.Immutable
import dev.sargunv.maplibrecompose.core.expression.Expression

/**
 * @param minZoom Minimum zoom level at which to create vector tiles (lower means more field of view
 *   detail at low zoom levels).
 * @param maxZoom Maximum zoom level at which to create vector tiles (higher means greater detail at
 *   high zoom levels).
 * @param buffer Size of the tile buffer on each side. A value of 0 produces no buffer. A value of
 *   512 produces a buffer as wide as the tile itself. Larger values produce fewer rendering
 *   artifacts near tile edges at the cost of slower performance.
 * @param tolerance Douglas-Peucker simplification tolerance (higher means simpler geometries and
 *   faster performance).
 * @param cluster If the data is a collection of point features, setting this to `true` clusters the
 *   points by radius into groups. Cluster groups become new `Point` features in the source with
 *   additional properties:
 *     * `cluster`: Is `true` if the point is a cluster
 *     * `cluster_id`: A unique id for the cluster to be used in conjunction with the cluster
 *       inspection methods. (TODO which are not implemented yet on [GeoJsonSource] - #80)
 *     * `point_count`: Number of original points grouped into this cluster
 *     * `point_count_abbreviated`: An abbreviated point count
 *
 * @param clusterRadius Radius of each cluster when clustering points, measured in 1/512ths of a
 *   tile. I.e. a value of 512 indicates a radius equal to the width of a tile.
 * @param clusterMaxZoom Max zoom to cluster points on. Clusters are re-evaluated at integer zoom
 *   levels. So, setting the max zoom to 14 means that the clusters will still be displayed on zoom
 *   14.9.
 * @param clusterProperties A map defining custom properties on the generated clusters if clustering
 *   is enabled, aggregating values from clustered points. The keys are the property names, the
 *   values are expressions.
 *
 *   TODO examples missing, see https://maplibre.org/maplibre-style-spec/sources/#clusterproperties
 *
 * @param lineMetrics Whether to calculate line distance metrics. This is required for line layers
 *   that specify line-gradient values.
 */
// not supported yet:
// @param clusterMinPoints Minimum number of points necessary to form a cluster if clustering is
//   enabled.
@Immutable
public data class GeoJsonOptions(
  val minZoom: Int = 0,
  val maxZoom: Int = 18,
  val buffer: Int = 128,
  val tolerance: Float = 0.375f,
  val cluster: Boolean = false,
  val clusterRadius: Int = 50,
  // Not supported yet on Android, iOS: https://github.com/maplibre/maplibre-native/issues/261
  // val clusterMinPoints: Int = 2,
  val clusterMaxZoom: Int = maxZoom - 1,
  val clusterProperties: Map<String, ClusterProperty> = emptyMap(),
  val lineMetrics: Boolean = false,
) {
  // TODO seems to contradict https://maplibre.org/maplibre-style-spec/sources/#clusterproperties
  public data class ClusterProperty(val operator: String, val mapper: Expression<*>)
}
