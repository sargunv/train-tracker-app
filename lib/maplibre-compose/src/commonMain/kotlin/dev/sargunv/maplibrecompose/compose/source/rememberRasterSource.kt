package dev.sargunv.maplibrecompose.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key as composeKey
import dev.sargunv.maplibrecompose.core.source.RasterSource
import dev.sargunv.maplibrecompose.core.source.Source

@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun rememberRasterSource(id: String, uri: String): Source =
  composeKey(id, uri) {
    rememberUserSource(factory = { RasterSource(id = id, uri = uri) }, update = {})
  }
