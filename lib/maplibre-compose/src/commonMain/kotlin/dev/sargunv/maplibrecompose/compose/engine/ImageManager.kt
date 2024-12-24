package dev.sargunv.maplibrecompose.compose.engine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.ExpressionValue
import dev.sargunv.maplibrecompose.core.expression.ExpressionsDsl.cast
import dev.sargunv.maplibrecompose.core.expression.ResolvedValue

internal class ImageManager(private val node: StyleNode) {
  private val idMap = IncrementingIdMap<ImageBitmap>("image")

  private val counter =
    ReferenceCounter<ImageBitmap>(
      onZeroToOne = { image ->
        val id = idMap.addId(image)
        node.logger?.i { "Adding image $id" }
        node.style.addImage(id, image)
      },
      onOneToZero = { image ->
        val id = idMap.removeId(image)
        node.logger?.i { "Removing image $id" }
        node.style.removeImage(id)
      },
    )

  private fun addReference(image: ImageBitmap): String {
    counter.increment(image)
    return idMap.getId(image)
  }

  private fun removeReference(image: ImageBitmap) {
    counter.decrement(image)
  }

  @Composable
  internal fun <T : ExpressionValue> resolveImages(
    expr: Expression<T>
  ): Expression<ResolvedValue<T>> {
    DisposableEffect(expr) {
      onDispose { expr.visitLeaves { if (it is ImageBitmap) removeReference(it) } }
    }
    return remember(expr) {
      expr
        .mapLeaves {
          when (it) {
            is ImageBitmap -> addReference(it)
            else -> it
          }
        }
        .cast()
    }
  }
}
