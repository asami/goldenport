package org.goldenport.entities.xmind

import org.goldenport.value._
import java.util.UUID

/**
 * @since   Feb.  2, 2009
 *  version Feb.  2, 2009
 * @since   Feb.  2, 2009
 * @author  ASAMI, Tomoharu
 */
abstract class XMindNode(aName: String) extends GTreeNodeBase[XMindNode] {
  type TreeNode_TYPE = XMindNode
  set_name(aName)
  content = this

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
