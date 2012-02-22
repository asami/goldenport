package org.goldenport.entities.json

import org.goldenport.value._
import java.util.UUID

/**
 * @since   Nov. 30, 2011
 *  version Nov. 30, 2011
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class JsonNode(aName: String) extends GTreeNodeBase[JsonNode] {
  type TreeNode_TYPE = JsonNode
  set_name(aName)
  content = this

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TopicNode(aName)
  }
}

class RootNode extends JsonNode(null) {
}

class SheetNode(aName: String) extends JsonNode(aName) {
}

class TopicNode(aName: String) extends JsonNode(aName) {
}
