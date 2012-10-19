package org.goldenport.entities.xmind

import org.goldenport.value._
import java.util.UUID

/**
 * @since   Feb.  2, 2009
 * @version Oct. 19, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class XMindNode(aName: String) extends GTreeNodeBase[XMindNode] {
  type TreeNode_TYPE = XMindNode
  set_name(aName)
  content = this

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new XTopicNode(aName)
  }
}

@deprecated("use outline node", "0.5")
class XRootNode extends XMindNode(null) {
}

@deprecated("use outline node", "0.5")
class XSheetNode(aName: String) extends XMindNode(aName) {
}

@deprecated("use outline node", "0.5")
class XTopicNode(aName: String) extends XMindNode(aName) {
}
