package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.content.EntityContent
import org.goldenport.value.GTree
import org.goldenport.value.GTreeBase
import org.goldenport.value.GTreeNode

/*
 * @since   Aug. 17, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class GTreeContainerEntityBase(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeContainerEntity(aIn, aOut, aContext) with GTreeBase[GContent] {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)

  final override def open_Post_Condition() {
    assert(root != null)
  }

  final def setEntity(aPath: String, aEntity: GEntity): TreeNode_TYPE = {
    require(aPath != null && aEntity != null)
    setContent(aPath, aEntity.toContent)
  }

  final def getEntity[GE <: GEntity](aPath: String): Option[GE] = {
    val mayNode = getNode(aPath)
    if (mayNode.isEmpty) return None
    val node = mayNode.get
    if (node.mountedEntity.isDefined)
      return node.mountedEntity.asInstanceOf[Some[GE]]
    if (node.content.isInstanceOf[EntityContent])
	return Some(node.content.asInstanceOf[EntityContent].entity.asInstanceOf[GE])
    node.mount().asInstanceOf[Option[GE]]
  }

  final override def set_Modified() {
    set_dirty()
  }

  final override def entity_Commit() {
    traverse(new GTreeContainerVisitor {
      override def startEnter(aNode: GTreeContainerEntityNode): Unit = {
	aNode.commit()
//    traverse(n => println("entity_Commit: " + n))
      }
    })
  }

  final override def copy_Node(aSource: GTreeNode[GContent], aTarget: GTreeNode[GContent]) {
    copy_node(aSource.asInstanceOf[GTreeContainerEntityNode],
	      aTarget.asInstanceOf[GTreeContainerEntityNode])
  }

  private def copy_node(aSource: GTreeContainerEntityNode, aTarget: GTreeContainerEntityNode) {
    val mayEntity = aSource.mountedEntity
//    println("copy_node : " + aSource + "/" + mayEntity)
    if (mayEntity.isDefined) {
      aTarget.content = mayEntity.get.toContent
    } else if (aSource.content != null) { // in case of null, should over assignment ?
      aTarget.content = aSource.content
    } else {
//      val mayIn = aSource.getInputDataSource() 2009-03-12
//      if (mayIn.isDefined) {
//	aTarget.setInputDataSource(mayIn.get)
//      }
    }
  }

  final override def open_Source(aSource: GTree[GContent]) {
    aSource.asInstanceOf[GTreeContainerEntity].open()
  }

  final override def close_Source(aSource: GTree[GContent]) {
    aSource.asInstanceOf[GTreeContainerEntity].close()
  }
}
