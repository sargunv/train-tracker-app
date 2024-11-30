package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.IlluminationAnchor
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import org.maplibre.android.style.layers.HillshadeLayer as MLNHillshadeLayer
import org.maplibre.android.style.layers.PropertyFactory

@PublishedApi
internal actual class HillshadeLayer actual constructor(id: String, actual val source: Source) :
  Layer() {
  override val impl = MLNHillshadeLayer(id, source.id)

  actual fun setHillshadeIlluminationDirection(direction: Expression<Number>) {
    impl.setProperties(PropertyFactory.hillshadeIlluminationDirection(direction.toMLNExpression()))
  }

  actual fun setHillshadeIlluminationAnchor(anchor: Expression<IlluminationAnchor>) {
    impl.setProperties(PropertyFactory.hillshadeIlluminationAnchor(anchor.toMLNExpression()))
  }

  actual fun setHillshadeExaggeration(exaggeration: Expression<Number>) {
    impl.setProperties(PropertyFactory.hillshadeExaggeration(exaggeration.toMLNExpression()))
  }

  actual fun setHillshadeShadowColor(shadowColor: Expression<Color>) {
    impl.setProperties(PropertyFactory.hillshadeShadowColor(shadowColor.toMLNExpression()))
  }

  actual fun setHillshadeHighlightColor(highlightColor: Expression<Color>) {
    impl.setProperties(PropertyFactory.hillshadeHighlightColor(highlightColor.toMLNExpression()))
  }

  actual fun setHillshadeAccentColor(accentColor: Expression<Color>) {
    impl.setProperties(PropertyFactory.hillshadeAccentColor(accentColor.toMLNExpression()))
  }
}
