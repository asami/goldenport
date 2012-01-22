package org.goldenport.value

import scala.xml.Node

/*
 * @since   Aug. 12, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
trait GTreeBase[E] extends GTree[E] {
  private var root_node: TreeNode_TYPE = _
  private var is_modified: Boolean = false

  protected final def set_root(aRoot: TreeNode_TYPE) {
    require (aRoot != null)
    root_node = aRoot
    root_node.facade = this
  }

  protected final def set_root {
    set_root(new PlainTreeNode[E].asInstanceOf[TreeNode_TYPE])
  }

  final def root: TreeNode_TYPE = {
    require (root_node != null)
    return root_node
  }

  def isModified: Boolean = is_modified

  final def setModified() { // Visibility
    is_modified = true
    set_Modified()
  }

  def set_Modified(): Unit = null

  final def clearModified() { is_modified = false } // Visibility

  final def getNode(path: String): Option[TreeNode_TYPE] = {
    require(path != null)
    assert(root_node != null)
    root_node.getNode(path).asInstanceOf[Option[TreeNode_TYPE]]
  }

  final def getContent(path: String): Option[E] = {
    require(path != null)
    assert(root_node != null)
    val mayNode = root_node.getNode(path)
    if (mayNode.isEmpty) None
    else if (mayNode.get.content != null) Some(mayNode.get.content)
    else None
  }

  final def setNode(path: String): TreeNode_TYPE = {
    require(path != null)
    assert(root_node != null)
    root_node.setNode(path).asInstanceOf[TreeNode_TYPE]
  }

  final def setContent(path: String, content: E): TreeNode_TYPE = {
    require(path != null)
    assert(root_node != null)
    root_node.setContent(path, content).asInstanceOf[TreeNode_TYPE]
  }

/*
  final def setContent(path: String, content: E): E = {
    require(path != null)
    assert(root_node != null)
    root_node.setNode(path, content)
    content
  }
*/

  final def copyIn(aSource: GTree[E]) {
    require(aSource != null)
    assert(root_node != null)
    open_Source(aSource)
    copy_in(aSource.root, root)
    close_Source(aSource)
  }

  final def copyIn(aPathname: String, aSource: GTree[E]) {
    require(aSource != null)
    assert(root_node != null)
    open_Source(aSource)
    copy_in(aSource.root, setNode(aPathname))
    close_Source(aSource)
  }

  private def copy_in(aSource: GTreeNode[E], aTarget: GTreeNode[E]) {
    for (sourceChild <- aSource.children) {
      val targetChild = aTarget.setChild(sourceChild.name)
      copy_Node(sourceChild, targetChild)
      copy_in(sourceChild, targetChild)
    }
  }

  protected def copy_Node(aSource: GTreeNode[E], aTarget: GTreeNode[E]) {
    sys.error("missing implementation copy_Node(GTreeNode[E], GTreeNode[E]) : " + this)
  }

  protected def open_Source(aSource: GTree[E]): Unit = null

  protected def close_Source(aSource: GTree[E]): Unit = null

  final def traverse(visitor: GTreeVisitor[E]) {
    root_node.traverse(visitor)
  }

  final def traverse(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean) {
    root_node.traverse(visitor, filter)
  }

  final def traverse(aFunction: E => Unit) {
    traverse(new FunctionVisitor[E](aFunction))
  }

  final def collect(aFilter: GTreeNode[E] => Boolean): Seq[GTreeNode[E]] = {
    val collector = new GTreeCollector[E](aFilter)
    traverse(collector)
    collector.result
  }

  def print {
    traverse(new GTreeVisitor[E] {
      override def startEnter(node: GTreeNode[E]) {
    	println(node.pathname)
      }
    })
  }

  final def cursor: GTreeCursor[E] = {
    new GTreeCursor[E](this)
  }

  final def toXml: Node = {
    assert(root_node != null)
    root_node.toXml
  }

  final def toPrettyXml: String = {
    assert(root_node != null)
    root_node.toPrettyXml
  }
}
