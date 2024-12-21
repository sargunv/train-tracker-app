package dev.sargunv.maplibrecompose.core

import dev.sargunv.maplibrecompose.core.layer.Layer
import dev.sargunv.maplibrecompose.core.layer.UnknownLayer
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.source.UnknownSource
import org.maplibre.android.maps.Style as MLNStyle

internal class AndroidStyle(style: MLNStyle) : Style {
  private var impl: MLNStyle = style

  override fun getImage(id: String): Image? {
    return impl.getImage(id)?.let { Image(id, it) }
  }

  override fun addImage(image: Image) {
    impl.addImage(image.id, image.impl)
  }

  override fun removeImage(image: Image) {
    impl.removeImage(image.id)
  }

  override fun getSource(id: String): Source? {
    return impl.getSource(id)?.let { UnknownSource(it) }
  }

  override fun getSources(): List<Source> {
    return impl.sources.map { UnknownSource(it) }
  }

  override fun addSource(source: Source) {
    impl.addSource(source.impl)
  }

  override fun removeSource(source: Source) {
    impl.removeSource(source.impl)
  }

  override fun getLayer(id: String): Layer? {
    return impl.getLayer(id)?.let { UnknownLayer(it) }
  }

  override fun getLayers(): List<Layer> {
    return impl.layers.map { UnknownLayer(it) }
  }

  override fun addLayer(layer: Layer) {
    impl.addLayer(layer.impl)
  }

  override fun addLayerAbove(id: String, layer: Layer) {
    impl.addLayerAbove(layer.impl, id)
  }

  override fun addLayerBelow(id: String, layer: Layer) {
    impl.addLayerBelow(layer.impl, id)
  }

  override fun addLayerAt(index: Int, layer: Layer) {
    impl.addLayerAt(layer.impl, index)
  }

  override fun removeLayer(layer: Layer) {
    impl.removeLayer(layer.impl)
  }
}
