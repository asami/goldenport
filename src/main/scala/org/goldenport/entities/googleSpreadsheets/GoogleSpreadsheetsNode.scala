package org.goldenport.entities.googleSpreadsheets

import org.goldenport.value._
import java.util.UUID

/**
 * @since   Nov. 30, 2011
 *  version Nov. 30, 2011
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GoogleSpreadsheetsNode(aName: String) extends GTreeNodeBase[GoogleSpreadsheetsNode] {
  type TreeNode_TYPE = GoogleSpreadsheetsNode
  set_name(aName)
  content = this

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TopicNode(aName)
  }
}

class RootNode extends GoogleSpreadsheetsNode(null) {
}

class SheetNode(aName: String) extends GoogleSpreadsheetsNode(aName) {
}

class TopicNode(aName: String) extends GoogleSpreadsheetsNode(aName) {
}
