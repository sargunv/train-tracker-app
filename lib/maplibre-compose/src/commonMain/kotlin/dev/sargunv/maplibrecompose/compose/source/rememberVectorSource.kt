package dev.sargunv.maplibrecompose.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key as composeKey
import dev.sargunv.maplibrecompose.core.source.VectorSource

/**
 *  Remember a new [VectorSource] with the given [id] from the given [configUrl].
 *
 *  @throws IllegalArgumentException if a layer with the given [id] already exists.
 * */
@Composable
@Suppress("NOTHING_TO_INLINE")
public inline fun rememberVectorSource(id: String, configUrl: String): VectorSource =
  composeKey(id, configUrl) {
    rememberUserSource(factory = { VectorSource(id = id, configUrl = configUrl) }, update = {})
  }
