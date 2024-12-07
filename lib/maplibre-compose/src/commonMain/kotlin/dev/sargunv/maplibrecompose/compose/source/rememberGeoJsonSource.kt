package dev.sargunv.maplibrecompose.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import dev.sargunv.maplibrecompose.core.source.GeoJsonOptions
import dev.sargunv.maplibrecompose.core.source.GeoJsonSource
import io.github.dellisd.spatialk.geojson.GeoJson

/**
 * Remember a new [GeoJsonSource] with the given [id] and [options] from the GeoJson data at the
 * given [dataUrl].
 *
 * @throws IllegalArgumentException if a layer with the given [id] already exists.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun rememberGeoJsonSource(
  id: String,
  dataUrl: String,
  options: GeoJsonOptions = GeoJsonOptions(),
): GeoJsonSource =
  key(id, options) {
    rememberUserSource(
      factory = { GeoJsonSource(id = id, dataUrl = dataUrl, options = options) },
      update = { setDataUrl(dataUrl) },
    )
  }

/**
 * Remember a new [GeoJsonSource] with the given [id] and [options] from the given GeoJson [data].
 *
 * @throws IllegalArgumentException if a layer with the given [id] already exists.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun rememberGeoJsonSource(
  id: String,
  data: GeoJson,
  options: GeoJsonOptions = GeoJsonOptions(),
): GeoJsonSource =
  key(id, options) {
    rememberUserSource(
      factory = { GeoJsonSource(id = id, data = data, options = options) },
      update = { setData(data) },
    )
  }
