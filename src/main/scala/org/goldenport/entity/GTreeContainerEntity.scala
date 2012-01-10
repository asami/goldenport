package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.value.{GTree, GTreeNode}

/*
 * Aug.  5, 2008
 * Oct.  7, 2008
 */
abstract class GTreeContainerEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GContainerEntity(aIn, aOut, aContext) with GTree[GContent] {
  type TreeNode_TYPE <: GTreeContainerEntityNode

  final def setString(aPathname: String, aString: String): TreeNode_TYPE = {
    setContent(aPathname, GContent.create(aString, entityContext))
  }
  
  final def setReference(aPathname: String, aReference: String): TreeNode_TYPE = {
    setContent(aPathname, entityContext.makeReferenceContent(aReference))
  }

  def setEntity(aPath: String, aEntity: GEntity): TreeNode_TYPE
  def getEntity[GE <: GEntity](aPath: String): Option[GE]

  final def mount(aPathname: String, anEntity: GTreeContainerEntity) {
    setNode(aPathname).mount(anEntity)
  }

  final def copyInValue[V](aSource: GTree[V]) {
    require(aSource != null)
    if (aSource.isInstanceOf[GTreeEntity[V]]) {
      copyInValueEntity(aSource.asInstanceOf[GTreeEntity[V]])
    } else {
      copy_in_value(aSource)
    }
  }

  final def copyInValueEntity[V](aSource: GTreeEntity[V]) {
    aSource.open()
    try {
      copy_in_value(aSource)
    } finally {
      aSource.close()
    }
  }

  private def copy_in_value[V](aSource: GTree[V]) {
    copy_in_value(aSource.root, root)
  }

  private def copy_in_value[V](aSource: GTreeNode[V], aTarget: GTreeContainerEntityNode) {
    for (sourceChild <- aSource.children) {
      val targetChild = aTarget.setChild(sourceChild.name)
      aTarget.content = GContent.create(sourceChild.content, entityContext)
      copy_in_value(sourceChild, targetChild)
    }
  }
}
