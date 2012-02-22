package org.goldenport.value

import scala.xml.Node
import scalaz._
import Scalaz._

/*
 * @since   Jul. 27, 2008
 *  version Apr. 17, 2011
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
trait GTreeNode[E] {
  type TreeNode_TYPE <: GTreeNode[E]

  def name: String
  def title: String
  def title_=(s: String)
  def content: E
  def content_=(aContent: E)
  def parent: TreeNode_TYPE
  def parent_=(aParent: GTreeNode[E]): Unit // visibility
  def facade: GTree[E]
  def facade_=(aFacade: GTree[E]): Unit // visibility
  def isModified: Boolean
  def setModified(): Unit // visibility
  def isRoot: Boolean
  def isContainer: Boolean
  def isLeaf: Boolean
  def isEmpty: Boolean
  def length: Int
  def children: Seq[TreeNode_TYPE]
  def indexOf(node: GTreeNode[E]): Int
  def getChild(index: Int): TreeNode_TYPE
  def getChild(name: String): Option[TreeNode_TYPE]
  def setChild(name: String): TreeNode_TYPE
  def addChild(): TreeNode_TYPE
  def addChild(child: GTreeNode[E]): TreeNode_TYPE
  def addChildren(parent: GTreeNode[E]): Unit
  def addContent(content: E): TreeNode_TYPE
  def removeChild(child: GTreeNode[E])
  def getNode(pathname: String): Option[TreeNode_TYPE]
  def getNode(pathname: GPathname): Option[TreeNode_TYPE]
  def setNode(pathname: String): TreeNode_TYPE
  def setNode(pathname: GPathname): TreeNode_TYPE
  def setContent(pathname: String, content: E): TreeNode_TYPE
  def setContent(pathname: GPathname, content: E): TreeNode_TYPE
  def clear(): Unit
  def traverse(visitor: GTreeVisitor[E])
  def traverse(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean)
  def traverseNode(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean)
  def traverseChildren(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean)
  def traverse(aFunction: E => Unit)
  def cursor: GTreeCursor[E]
  def deepCopy: TreeNode_TYPE
  def toText: String
  def buildText(buffer: StringBuilder): StringBuilder
  def toXml: Node
  def toPrettyXml: String

  /*
   * XXX pathname format variation - absolute, container
   */
  def pathname: String = {
    val pb = new GPathnameBuffer
    pb.absolute = true
//    pb.container = isContainer
    var node: GTreeNode[E] = this
    while (!node.isRoot) {
      pb.addContainer(node.name)
      node = node.parent
    }
    pb.toString
  }
}

class GTreeNodeShow[E] extends Show[GTreeNode[E]] {
  def show(a: GTreeNode[E]) = {
    val d = for {
      b <- Option(a)
      c <- Option(b.name)
    } yield c
    (d | "-").toList
  }
}