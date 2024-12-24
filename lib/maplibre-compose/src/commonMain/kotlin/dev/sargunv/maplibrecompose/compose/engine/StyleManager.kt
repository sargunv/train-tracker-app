package dev.sargunv.maplibrecompose.compose.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrecompose.core.Style
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.ExpressionValue
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.cast
import dev.sargunv.maplibrecompose.core.expression.ResolvedValue

internal class StyleManager(var style: Style, internal var logger: Logger?) {
  internal val layerManager = LayerManager(this)
  internal val sourceManager = SourceManager(this)
  internal val imageManager = ImageManager(this)

  internal fun applyChanges() {
    sourceManager.applyChanges()
    layerManager.applyChanges()
  }

  @Composable
  internal fun <T : ExpressionValue> rememberResolved(
    expr: Expression<T>
  ): Expression<ResolvedValue<T>> {
    DisposableEffect(expr) {
      onDispose { expr.visitLeaves { if (it is ImageBitmap) imageManager.removeReference(it) } }
    }
    return remember(expr) {
      expr
        .mapLeaves {
          when (it) {
            is ImageBitmap -> imageManager.addReference(it)
            else -> it
          }
        }
        .cast()
    }
  }
}
