package org.goldenport.value

/*
 * Jul. 28, 2008
 * Aug. 26, 2008
 */
class PlainTreeNode[E](aName: String) extends GTreeNodeBase[E] {
  type TreeNode_TYPE = PlainTreeNode[E]
  set_name(aName)

  def this() = this(null)

  def new_Node(name: String): TreeNode_TYPE = {
    new PlainTreeNode[E](name)
  }
}
