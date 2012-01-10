package org.goldenport.entities.xmind

import org.goldenport.value._
import java.util.UUID

/*
 * Feb.  2, 2009
 * Feb.  2, 2009
 */
abstract class XMindNode(aName: String) extends GTreeNodeBase[XMindNode] {
  type TreeNode_TYPE = XMindNode
  set_name(aName)
  content = this

  var id: String = UUID.randomUUID.toString
  var title: String = ""

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TopicNode(aName)
  }
}

class RootNode extends XMindNode(null) {
}

class SheetNode(aName: String) extends XMindNode(aName) {
}

class TopicNode(aName: String) extends XMindNode(aName) {
}
