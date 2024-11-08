package dev.sargunv.maplibrekmp.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrekmp.internal.MapNode
import dev.sargunv.maplibrekmp.internal.MapNodeApplier
import dev.sargunv.maplibrekmp.internal.wrapper.layer.LineLayer
import dev.sargunv.maplibrekmp.internal.wrapper.source.GeoJsonSource
import dev.sargunv.maplibrekmp.internal.wrapper.source.Source
import dev.sargunv.maplibrekmp.style.expression.Expression
import dev.sargunv.maplibrekmp.style.expression.ExpressionScope
import kotlin.random.Random

@DslMarker internal annotation class MapDsl

@MapDsl public data object StyleScope : LayerStackScope

@MapDsl public sealed interface LayerStackScope : ExpressionScope

private var incrementingId = Random.nextInt() % 1000 + 1000

private fun nextId(): String = "maplibrekmp-${incrementingId++}"

@Composable
internal fun getSource(id: String): Source? {
  val rootNode = ((currentComposer.applier as MapNodeApplier).root as MapNode.StyleNode)
  return rootNode.getSource(id).value
}

@Composable
public fun StyleScope.GeoJsonSource(url: String, tolerance: Float? = null): String {
  val id = remember { nextId() }
  ComposeNode<MapNode.SourceNode<GeoJsonSource>, MapNodeApplier>(
    factory = { MapNode.SourceNode(GeoJsonSource(id = id, url = url, tolerance = tolerance)) },
    update = {},
  )
  return id
}

@Composable
public fun LayerStackScope.LineLayer(
  sourceId: String,
  lineColor: Expression<Color>,
  lineWidth: Expression<Number>,
) {
  val id = remember { nextId() }
  getSource(sourceId)?.let { source ->
    ComposeNode<MapNode.LayerNode<LineLayer>, MapNodeApplier>(
      factory = { MapNode.LayerNode(LineLayer(id = id, source = source)) },
      update = {
        set(lineColor) { layer.setColor(it) }
        set(lineWidth) { layer.setWidth(it) }
      },
    )
  }
}

@Composable
public fun StyleScope.AnchoredLayers(
  anchor: LayerAnchor,
  content: @Composable LayerStackScope.() -> Unit,
) {
  ComposeNode<MapNode.LayerStackNode, MapNodeApplier>(
    factory = { MapNode.LayerStackNode(anchor) },
    update = {},
  ) {
    content()
  }
}
