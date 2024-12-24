package dev.sargunv.maplibrecompose.compose.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.staticCompositionLocalOf
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrecompose.compose.MaplibreComposable
import dev.sargunv.maplibrecompose.core.Style
import kotlinx.coroutines.awaitCancellation

@Composable
internal fun rememberStyleComposition(
  maybeStyle: Style?,
  logger: Logger?,
  content: @Composable @MaplibreComposable () -> Unit,
): State<StyleNode?> {
  val nodeState = remember { mutableStateOf<StyleNode?>(null) }
  val compositionContext = rememberCompositionContext()

  LaunchedEffect(maybeStyle, compositionContext) {
    val style = maybeStyle ?: return@LaunchedEffect
    val rootNode = StyleNode(style, logger).also { nodeState.value = it }
    val composition = Composition(MapNodeApplier(rootNode), compositionContext)

    composition.setContent {
      CompositionLocalProvider(LocalStyleManager provides rootNode) { content() }
    }

    try {
      awaitCancellation()
    } finally {
      nodeState.value = null
      rootNode.style = Style.Null
      composition.dispose()
    }
  }

  return nodeState
}

internal val LocalStyleManager =
  staticCompositionLocalOf<StyleNode> { throw IllegalStateException() }
