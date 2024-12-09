package dev.sargunv.maplibrecompose.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import dev.sargunv.maplibrecompose.compose.engine.LocalStyleManager
import dev.sargunv.maplibrecompose.core.source.Source

@Composable
@PublishedApi
internal fun <T : Source> rememberUserSource(factory: () -> T, update: T.() -> Unit): T {
  val styleManager = LocalStyleManager.current
  val source = remember(styleManager) { factory().also { styleManager.addSource(it) } }
  remember(source, update) { source.update() }
  DisposableEffect(styleManager, source) { onDispose { styleManager.removeSource(source) } }
  return source
}
