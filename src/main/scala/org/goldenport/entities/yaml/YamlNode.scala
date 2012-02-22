package org.goldenport.entities.yaml

import org.goldenport.value._
import java.util.UUID

/**
 * @since   Nov. 29, 2011
 *  version Nov. 29, 2011
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class YamlNode(aName: String) extends GTreeNodeBase[YamlNode] {
  type TreeNode_TYPE = YamlNode
  set_name(aName)
  content = this

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TopicNode(aName)
  }
}

class RootNode extends YamlNode(null) {
}

class SheetNode(aName: String) extends YamlNode(aName) {
}

class TopicNode(aName: String) extends YamlNode(aName) {
}
