package dev.sargunv.maplibrekmp.core.layer

import cocoapods.MapLibre.MLNStyleLayer

@PublishedApi
internal actual sealed class Layer {
  abstract val impl: MLNStyleLayer
  actual val id: String
    get() = impl.identifier

  actual var minZoom: Float
    get() = impl.minimumZoomLevel
    set(value) {
      impl.minimumZoomLevel = value
    }

  actual var maxZoom: Float
    get() = impl.maximumZoomLevel
    set(value) {
      impl.maximumZoomLevel = value
    }

  actual var visible: Boolean
    get() = impl.visible
    set(value) {
      impl.visible = value
    }

  override fun toString() = "${this::class.simpleName}(id=\"$id\")"
}
