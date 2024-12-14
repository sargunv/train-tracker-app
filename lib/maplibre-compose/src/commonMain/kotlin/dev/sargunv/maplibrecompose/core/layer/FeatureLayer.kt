package dev.sargunv.maplibrecompose.core.layer

import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.source.Source

@PublishedApi
internal expect sealed class FeatureLayer(source: Source) : Layer {
  val source: Source
  abstract var sourceLayer: String

  abstract fun setFilter(filter: Expression<BooleanValue>)
}
