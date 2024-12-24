package dev.sargunv.maplibrecompose.core

import androidx.compose.ui.graphics.ImageBitmap
import cocoapods.MapLibre.MLNSource
import cocoapods.MapLibre.MLNStyle
import cocoapods.MapLibre.MLNStyleLayer
import dev.sargunv.maplibrecompose.core.layer.Layer
import dev.sargunv.maplibrecompose.core.layer.UnknownLayer
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.source.UnknownSource
import dev.sargunv.maplibrecompose.core.util.toUIImage

internal class IosStyle(style: MLNStyle, private val getScale: () -> Float) : Style {
  private var impl: MLNStyle = style

  override fun addImage(id: String, image: ImageBitmap) {
    impl.setImage(image.toUIImage(getScale()), forName = id)
  }

  override fun removeImage(id: String) {
    impl.removeImageForName(id)
  }

  override fun getSource(id: String): Source? {
    return impl.sourceWithIdentifier(id)?.let { UnknownSource(it) }
  }

  override fun getSources(): List<Source> {
    return impl.sources.map { UnknownSource(it as MLNSource) }
  }

  override fun addSource(source: Source) {
    impl.addSource(source.impl)
  }

  override fun removeSource(source: Source) {
    impl.removeSource(source.impl)
  }

  override fun getLayer(id: String): Layer? {
    return impl.layerWithIdentifier(id)?.let { UnknownLayer(it) }
  }

  override fun getLayers(): List<Layer> {
    return impl.layers.map { UnknownLayer(it as MLNStyleLayer) }
  }

  override fun addLayer(layer: Layer) {
    impl.addLayer(layer.impl)
  }

  override fun addLayerAbove(id: String, layer: Layer) {
    impl.insertLayer(layer.impl, aboveLayer = impl.layerWithIdentifier(id)!!)
  }

  override fun addLayerBelow(id: String, layer: Layer) {
    impl.insertLayer(layer.impl, belowLayer = impl.layerWithIdentifier(id)!!)
  }

  override fun addLayerAt(index: Int, layer: Layer) {
    impl.insertLayer(layer.impl, atIndex = index.toULong())
  }

  override fun removeLayer(layer: Layer) {
    impl.removeLayer(layer.impl)
  }
}
