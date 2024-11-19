package dev.sargunv.maplibrekmp.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import dev.sargunv.maplibrekmp.compose.engine.LocalStyleManager
import dev.sargunv.maplibrekmp.core.source.Source

@Composable
@PublishedApi
internal fun <T : Source> rememberUserSource(factory: () -> T, update: T.() -> Unit): Source {
  val styleManager = LocalStyleManager.current
  val source = remember(styleManager) { factory().also { styleManager.addSource(it) } }
  remember(source, update) { source.update() }
  DisposableEffect(styleManager, source) { onDispose { styleManager.removeSource(source) } }
  return source
}
