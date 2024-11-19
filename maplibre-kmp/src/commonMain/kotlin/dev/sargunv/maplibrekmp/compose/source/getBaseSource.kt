package dev.sargunv.maplibrekmp.compose.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.sargunv.maplibrekmp.compose.engine.LocalStyleManager
import dev.sargunv.maplibrekmp.core.source.Source

@Composable
public fun getBaseSource(id: String): Source {
  val styleManager = LocalStyleManager.current
  return remember(styleManager, id) { styleManager.getBaseSource(id) }
}
