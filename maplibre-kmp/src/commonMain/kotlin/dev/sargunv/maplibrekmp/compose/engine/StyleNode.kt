package dev.sargunv.maplibrekmp.compose.engine

internal class StyleNode(internal val styleManager: StyleManager) : MapNode() {

  override fun allowsChild(node: MapNode) = node is LayerNode<*>

  override fun onChildRemoved(oldIndex: Int, node: MapNode) {
    node as LayerNode<*>
    styleManager.removeLayer(node, oldIndex)
  }

  override fun onChildInserted(index: Int, node: MapNode) {
    node as LayerNode<*>
    styleManager.addLayer(node, index)
  }

  override fun onChildMoved(oldIndex: Int, index: Int, node: MapNode) {
    node as LayerNode<*>
    styleManager.moveLayer(node, oldIndex, index)
  }

  override fun onEndChanges() {
    styleManager.applyChanges()
  }
}
