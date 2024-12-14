package dev.sargunv.maplibrecompose.core.source

import dev.sargunv.maplibrecompose.core.expression.Expressions.const
import dev.sargunv.maplibrecompose.core.util.correctedAndroidUri
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import io.github.dellisd.spatialk.geojson.GeoJson
import org.maplibre.android.style.sources.GeoJsonOptions as MLNGeoJsonOptions
import org.maplibre.android.style.sources.GeoJsonSource as MLNGeoJsonSource

public actual class GeoJsonSource : Source {
  override val impl: MLNGeoJsonSource

  public actual constructor(id: String, uri: String, options: GeoJsonOptions) {
    impl = MLNGeoJsonSource(id, uri.correctedAndroidUri(), buildOptionMap(options))
  }

  public actual constructor(id: String, data: GeoJson, options: GeoJsonOptions) {
    impl = MLNGeoJsonSource(id, data.json(), buildOptionMap(options))
  }

  private fun buildOptionMap(options: GeoJsonOptions) =
    MLNGeoJsonOptions().apply {
      withMinZoom(options.minZoom)
      withMaxZoom(options.maxZoom)
      withBuffer(options.buffer)
      withTolerance(options.tolerance)
      withLineMetrics(options.lineMetrics)
      withCluster(options.cluster)
      withClusterMaxZoom(options.clusterMaxZoom)
      withClusterRadius(options.clusterRadius)
      options.clusterProperties.forEach { (key, value) ->
        withClusterProperty(
          key,
          const(value.operator).toMLNExpression()!!,
          value.mapper.toMLNExpression()!!,
        )
      }
    }

  public actual fun setUri(uri: String) {
    impl.setUri(uri.correctedAndroidUri())
  }

  public actual fun setData(geoJson: GeoJson) {
    impl.setGeoJson(geoJson.json())
  }
}
