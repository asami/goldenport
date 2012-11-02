package org.goldenport.value

import scala.xml.Node
import scalaz.Tree

/*
 * @since   Jul. 27, 2008
 *  version Apr. 17, 2011
 *  version Feb. 21, 2012
 *  version Apr. 30, 2012
 * @version Nov.  2, 2012
 * @author  ASAMI, Tomoharu
 */
trait GTree[E] {
  type TreeNode_TYPE <: GTreeNode[E]

  def root: TreeNode_TYPE
  def isModified: Boolean
  def setModified(): Unit // Visibility
  def clearModified(): Unit // Visibility
  def getNode(pathname: String): Option[TreeNode_TYPE]
  def getContent(path: String): Option[E]
  def setNode(pathname: String): TreeNode_TYPE
  def setContent(pathname: String, data: E): TreeNode_TYPE
  def copyIn(aSource: GTree[E]): Unit
  def traverse(visitor: GTreeVisitor[E])
  def traverse(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean)
  def traverse(aProcedure: E => Unit)
  def collect(aCollector: GTreeNode[E] => Boolean): Seq[GTreeNode[E]]
  //
  def collect[T](pf: PartialFunction[GTreeNode[E], T]): Seq[T]
  def collect[T](pathname: String, pf: PartialFunction[GTreeNode[E], T]): Seq[T]
  def traverse[T](pf: PartialFunction[GTreeNode[E], T]): Unit
  def traverse[T](pathname: String, pf: PartialFunction[GTreeNode[E], T]): Unit
  def collectContent[T](pf: PartialFunction[E, T]): Seq[T]
  def collectContent[T](pathname: String, pf: PartialFunction[E, T]): Seq[T]
  def traverseContent[T](pf: PartialFunction[E, T]): Unit
  def traverseContent[T](pathname: String, pf: PartialFunction[E, T]): Unit
  //
  def cursor: GTreeCursor[E]
  def toXml: Node
  def toPrettyXml: String
  def ztree: Tree[GTreeNode[E]]
}
