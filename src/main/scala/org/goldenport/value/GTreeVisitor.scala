package org.goldenport.value

import scala.collection.mutable.ArrayBuffer

/*
 * @since   Jul. 27, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
trait GTreeVisitor[E] {
  def startEnter(node: GTreeNode[E]): Unit = null // XXX better name
  def start(node: GTreeNode[E]): Unit = startEnter(node)
  def enter(node: GTreeNode[E]): Unit = startEnter(node)
  def stay(node: GTreeNode[E], index: Int, prev: GTreeNode[E], next: GTreeNode[E]): Unit = null
  def leave(node: GTreeNode[E]): Unit = leaveEnd(node)
  def end(node: GTreeNode[E]): Unit = leaveEnd(node)
  def leaveEnd(node: GTreeNode[E]): Unit = null

  private var _done_nodes: ArrayBuffer[GTreeNode[E]] = _

  private def done_nodes = {
    if (_done_nodes == null) _done_nodes = new ArrayBuffer[GTreeNode[E]]
    _done_nodes
  }

  protected final def done_traverse(aNode: GTreeNode[E]) {
    done_nodes += aNode
  }

  final def isDone(aNode: GTreeNode[E]): Boolean = {
    if (_done_nodes == null) false
    else done_nodes.contains(aNode)
  }
}
