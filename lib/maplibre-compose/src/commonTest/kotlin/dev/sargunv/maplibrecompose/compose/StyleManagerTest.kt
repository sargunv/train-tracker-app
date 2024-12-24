package dev.sargunv.maplibrecompose.compose

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.sargunv.maplibrecompose.compose.engine.LayerNode
import dev.sargunv.maplibrecompose.compose.engine.StyleManager
import dev.sargunv.maplibrecompose.compose.layer.Anchor
import dev.sargunv.maplibrecompose.core.layer.Layer
import dev.sargunv.maplibrecompose.core.layer.LineLayer
import dev.sargunv.maplibrecompose.core.source.GeoJsonOptions
import dev.sargunv.maplibrecompose.core.source.GeoJsonSource
import io.github.dellisd.spatialk.geojson.FeatureCollection
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

@OptIn(ExperimentalTestApi::class)
abstract class StyleManagerTest {
  private val testSources by lazy {
    listOf(
      GeoJsonSource("foo", FeatureCollection(), GeoJsonOptions()),
      GeoJsonSource("bar", FeatureCollection(), GeoJsonOptions()),
      GeoJsonSource("baz", FeatureCollection(), GeoJsonOptions()),
    )
  }

  private val testLayers by lazy {
    listOf(
      LineLayer("foo", testSources[0]),
      LineLayer("bar", testSources[1]),
      LineLayer("baz", testSources[2]),
    )
  }

  private fun makeStyleManager(): StyleManager {
    return StyleManager(FakeStyle(emptyList(), testSources, testLayers), null)
  }

  @BeforeTest open fun platformSetup() {}

  @Test
  fun shoudGetBaseSource() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      assertEquals(testSources[1], sm.getBaseSource("bar"))
      assertFails { sm.getBaseSource("BAR") }
    }
  }

  @Test
  fun shouldAddUserSource() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val newSource = GeoJsonSource("new", FeatureCollection(), GeoJsonOptions())
      sm.addSource(newSource)
      sm.applyChanges()
      assertEquals(4, sm.style.getSources().size)
      assertEquals(newSource, sm.style.getSource("new"))
    }
  }

  @Test
  fun shouldRemoveUserSource() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val newSource = GeoJsonSource("new", FeatureCollection(), GeoJsonOptions())
      sm.addSource(newSource)
      sm.applyChanges()
      sm.removeSource(newSource)
      assertEquals(3, sm.style.getSources().size)
      assertNull(sm.style.getSource("new"))
    }
  }

  @Test
  fun shouldNotReplaceBaseSource() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      assertFails { sm.addSource(GeoJsonSource("foo", FeatureCollection(), GeoJsonOptions())) }
    }
  }

  @Test
  fun shouldNotRemoveBaseSource() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      assertFails { sm.removeSource(testSources[1]) }
    }
  }

  @Test
  fun shouldAllowAddSourceBeforeRemove() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val s1 = GeoJsonSource("new", FeatureCollection(), GeoJsonOptions())
      val s2 = GeoJsonSource("new", FeatureCollection(), GeoJsonOptions())

      sm.addSource(s1)
      sm.applyChanges()

      assertEquals(s1, sm.style.getSource("new"))

      sm.addSource(s2)
      sm.removeSource(s1)
      sm.applyChanges()

      assertEquals(s2, sm.style.getSource("new"))
    }
  }

  @Test
  fun shouldAnchorTop() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val nodes = (0..2).map { LayerNode(LineLayer("new$it", testSources[0]), Anchor.Top) }
      nodes.forEachIndexed { i, node -> sm.addLayer(node, i) }
      sm.applyChanges()
      assertEquals(
        listOf("foo", "bar", "baz", "new0", "new1", "new2"),
        sm.style.getLayers().map(Layer::id),
      )
    }
  }

  @Test
  fun shouldAnchorBottom() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val nodes = (0..2).map { LayerNode(LineLayer("new$it", testSources[0]), Anchor.Bottom) }
      nodes.forEachIndexed { i, node -> sm.addLayer(node, i) }
      sm.applyChanges()
      assertEquals(
        listOf("new0", "new1", "new2", "foo", "bar", "baz"),
        sm.style.getLayers().map(Layer::id),
      )
    }
  }

  @Test
  fun shouldAnchorAbove() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val nodes = (0..2).map { LayerNode(LineLayer("new$it", testSources[0]), Anchor.Above("foo")) }
      nodes.forEachIndexed { i, node -> sm.addLayer(node, i) }
      sm.applyChanges()
      assertEquals(
        listOf("foo", "new0", "new1", "new2", "bar", "baz"),
        sm.style.getLayers().map(Layer::id),
      )
    }
  }

  @Test
  fun shouldAnchorBelow() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val nodes = (0..2).map { LayerNode(LineLayer("new$it", testSources[0]), Anchor.Below("baz")) }
      nodes.forEachIndexed { i, node -> sm.addLayer(node, i) }
      sm.applyChanges()
      assertEquals(
        listOf("foo", "bar", "new0", "new1", "new2", "baz"),
        sm.style.getLayers().map(Layer::id),
      )
    }
  }

  @Test
  fun shouldAnchorReplace() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val nodes =
        (0..2).map { LayerNode(LineLayer("new$it", testSources[0]), Anchor.Replace("bar")) }
      nodes.forEachIndexed { i, node -> sm.addLayer(node, i) }
      sm.applyChanges()
      assertEquals(
        listOf("foo", "new0", "new1", "new2", "baz"),
        sm.style.getLayers().map(Layer::id),
      )
    }
  }

  @Test
  fun shouldRestoreAfterReplace() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val nodes =
        (0..2).map { LayerNode(LineLayer("new$it", testSources[0]), Anchor.Replace("bar")) }

      nodes.forEachIndexed { i, node -> sm.addLayer(node, i) }
      sm.applyChanges()

      assertEquals(
        listOf("foo", "new0", "new1", "new2", "baz"),
        sm.style.getLayers().map(Layer::id),
      )

      nodes.forEach { node -> sm.removeLayer(node, 0) }
      sm.applyChanges()

      assertEquals(listOf("foo", "bar", "baz"), sm.style.getLayers().map(Layer::id))
    }
  }

  @Test
  fun shouldAllowAddLayerBeforeRemove() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()
      val l1 = LayerNode(LineLayer("new", testSources[0]), Anchor.Top)
      val l2 = LayerNode(LineLayer("new", testSources[1]), Anchor.Top)

      sm.addLayer(l1, 0)
      sm.applyChanges()

      assertEquals(l1.layer, sm.style.getLayer("new"))

      sm.addLayer(l2, 0)
      sm.removeLayer(l1, 1)
      sm.applyChanges()

      assertEquals(l2.layer, sm.style.getLayer("new"))
    }
  }

  @Test
  fun shouldMergeAnchors() = runComposeUiTest {
    runOnUiThread {
      val sm = makeStyleManager()

      sm.addLayer(LayerNode(LineLayer("b1", testSources[0]), Anchor.Bottom), 0)
      sm.addLayer(LayerNode(LineLayer("t1", testSources[0]), Anchor.Top), 0)
      sm.applyChanges()

      assertEquals(listOf("b1", "foo", "bar", "baz", "t1"), sm.style.getLayers().map(Layer::id))

      sm.addLayer(LayerNode(LineLayer("b2", testSources[0]), Anchor.Bottom), 0)
      sm.addLayer(LayerNode(LineLayer("t2", testSources[0]), Anchor.Top), 0)
      sm.applyChanges()

      assertEquals(
        listOf("b2", "b1", "foo", "bar", "baz", "t2", "t1"),
        sm.style.getLayers().map(Layer::id),
      )
    }
  }
}
