package dev.sargunv.maplibrecompose.core.source

import androidx.compose.runtime.Immutable
import dev.sargunv.maplibrecompose.core.expression.Expression

@Immutable
public data class GeoJsonOptions(
  val minZoom: Int = 0,
  val maxZoom: Int = 18,
  val buffer: Int = 128,
  val tolerance: Float = 0.375f,
  val cluster: Boolean = false,
  val clusterRadius: Int = 50,
  val clusterMaxZoom: Int = maxZoom - 1,
  val clusterProperties: Map<String, ClusterProperty> = emptyMap(),
  val lineMetrics: Boolean = false,
) {
  public data class ClusterProperty(val operator: String, val mapper: Expression<*>)
}
