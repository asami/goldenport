package org.goldenport.value

/*
 * Jul. 27, 2008
 * Aug. 12, 2008
 */
class PlainTree[E](node: GTreeNode[E]) extends GTreeBase[E] {
  type TreeNode_TYPE = GTreeNode[E]
  require(node != null)
  set_root(node)

  def this() = this(new PlainTreeNode[E]())
}
