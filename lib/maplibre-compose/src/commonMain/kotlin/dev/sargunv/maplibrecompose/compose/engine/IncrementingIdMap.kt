package dev.sargunv.maplibrecompose.compose.engine

internal class IncrementingIdMap<in T>(private val name: String) {
  private var nextId = 0
  private val map = mutableMapOf<T, String>()

  fun addId(value: T): String {
    return map.getOrPut(value) { "__MAPLIBRE_COMPOSE_${name}_${nextId++}" }
  }

  fun getId(value: T): String {
    return map[value] ?: error("id not found for value")
  }

  fun removeId(value: T): String {
    return map.remove(value) ?: error("id not found for value")
  }
}
