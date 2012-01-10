package org.goldenport.value

import scala.collection.mutable.ArrayBuffer
import java.util.UUID

/*
 * Aug. 13, 2008
 * Sep.  1, 2008
 */
trait GTreeNodeBase[E] extends GTreeNodeStructureBase[E] {
  private var node_name: String = _
  private var node_content: E = _

  protected final def set_name(aName: String) {
    require(node_name == null)
    node_name = aName
  }

  override def name: String = {
    if (node_name == null && parent != null) {
      node_name = UUID.randomUUID.toString()
    }
    node_name
  }
  override def content: E = node_content
  override def content_=(aContent: E) = node_content = aContent
}
