package dev.sargunv.maplibrecompose.compose.engine

import dev.sargunv.maplibrecompose.core.source.Source

internal class SourceManager(private val styleManager: StyleManager) {
  private val baseSources = styleManager.style.getSources().associateBy { it.id }

  private val sourcesToAdd = mutableListOf<Source>()

  private val counter =
    ReferenceCounter<Source>(
      onZeroToOne = { source ->
        styleManager.logger?.i { "Queuing source ${source.id} for addition" }
        sourcesToAdd.add(source)
      },
      onOneToZero = { source ->
        styleManager.logger?.i { "Removing source ${source.id}" }
        styleManager.style.removeSource(source)
      },
    )

  internal fun getBaseSource(id: String): Source {
    return baseSources[id] ?: error("Source ID '$id' not found in base style")
  }

  internal fun addReference(source: Source) {
    require(source.id !in baseSources) { "Source ID '${source.id}' already exists in base style" }
    counter.increment(source)
  }

  internal fun removeReference(source: Source) {
    require(source.id !in baseSources) {
      "Source ID '${source.id}' is part of the base style and can't be removed here"
    }
    counter.decrement(source)
  }

  internal fun applyChanges() {
    sourcesToAdd
      .onEach {
        styleManager.logger?.i { "Adding source ${it.id}" }
        styleManager.style.addSource(it)
      }
      .clear()
  }
}
