package dev.sargunv.maplibrecompose.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key as composeKey
import dev.sargunv.maplibrecompose.core.source.DEFAULT_RASTER_TILE_SIZE
import dev.sargunv.maplibrecompose.core.source.RasterSource

/**
 * Remember a new [RasterSource] with the given [id] and [tileSize] from the given [configUrl].
 *
 * @throws IllegalArgumentException if a layer with the given [id] already exists.
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun rememberRasterSource(
  id: String,
  configUrl: String,
  tileSize: Int = DEFAULT_RASTER_TILE_SIZE,
): RasterSource =
  composeKey(id, configUrl, tileSize) {
    rememberUserSource(
      factory = { RasterSource(id = id, configUrl = configUrl, tileSize = tileSize) },
      update = {},
    )
  }
