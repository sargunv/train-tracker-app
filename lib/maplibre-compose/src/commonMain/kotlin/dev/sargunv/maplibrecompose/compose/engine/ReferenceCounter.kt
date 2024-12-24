package dev.sargunv.maplibrecompose.compose.engine

/**
 * takes two function params, one for when the item is first incremented, the other when the
 * reference goes to zero
 */
internal class ReferenceCounter<in T>(
  private val onZeroToOne: (T) -> Unit = {},
  private val onOneToZero: (T) -> Unit = {},
) {
  private val map = mutableMapOf<T, Int>()

  fun increment(value: T) {
    val count = map[value]
    if (count == null) {
      map[value] = 1
      onZeroToOne(value)
    } else {
      map[value] = count + 1
    }
  }

  fun decrement(value: T) {
    val count = map[value] ?: error("decrementing below zero")
    if (count == 1) {
      map.remove(value)
      return onOneToZero(value)
    } else {
      map[value] = count - 1
    }
  }
}
