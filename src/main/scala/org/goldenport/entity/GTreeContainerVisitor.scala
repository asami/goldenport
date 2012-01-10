package org.goldenport.entity

import org.goldenport.value._
import org.goldenport.entity.content.GContent

/*
 * @since   Aug. 25, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
trait GTreeContainerVisitor extends GTreeVisitor[GContent] {
  def startEnter(aNode: GTreeContainerEntityNode): Unit = null
  def start(aNode: GTreeContainerEntityNode): Unit = startEnter(aNode)
  def enter(aNode: GTreeContainerEntityNode): Unit = startEnter(aNode)
  def stay(aNode: GTreeContainerEntityNode, anIndex: Int, aPrev: GTreeContainerEntityNode, aNext: GTreeContainerEntityNode): Unit = null
  def leave(aNode: GTreeContainerEntityNode): Unit = null
  def end(aNode: GTreeContainerEntityNode): Unit = null

  override def startEnter(aNode: GTreeNode[GContent]): Unit = {
    startEnter(aNode.asInstanceOf[GTreeContainerEntityNode])
  }

  override def start(aNode: GTreeNode[GContent]): Unit = {
    start(aNode.asInstanceOf[GTreeContainerEntityNode])
  }

  override def enter(aNode: GTreeNode[GContent]): Unit = {
    enter(aNode.asInstanceOf[GTreeContainerEntityNode])
  }

  override def stay(aNode: GTreeNode[GContent], anIndex: Int, aPrev: GTreeNode[GContent], aNext: GTreeNode[GContent]): Unit = {
    stay(aNode.asInstanceOf[GTreeContainerEntityNode], anIndex, aPrev.asInstanceOf[GTreeContainerEntityNode], aNext.asInstanceOf[GTreeContainerEntityNode])
  }

  override def leave(aNode: GTreeNode[GContent]): Unit = {
    leave(aNode.asInstanceOf[GTreeContainerEntityNode])
  }

  override def end(aNode: GTreeNode[GContent]): Unit = {
    end(aNode.asInstanceOf[GTreeContainerEntityNode])
  }
}
