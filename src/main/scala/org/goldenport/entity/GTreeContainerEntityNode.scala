package org.goldenport.entity

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content._
import org.goldenport.value.GTreeNodeStructureBase
import com.asamioffice.goldenport.text.UPathString

/*
 * Aug.  5, 2008
 * Feb.  6, 2009
 */
abstract class GTreeContainerEntityNode(aName: String, aEntity: GTreeContainerEntity) extends GContainerEntityNode(aName, aEntity) with GTreeNodeStructureBase[GContent] {
  type TreeNode_TYPE <: GTreeContainerEntityNode
  private val node_removes = mutable.Map.empty[String, GTreeContainerEntityNode]

  override def clear_Dirty = clearModified
  override def is_Dirty = isModified

  final def setString(aPathname: String, aString: String): TreeNode_TYPE = {
    setContent(aPathname, GContent.create(aString, container.entityContext))
  }

  final def setEntity(anEntity: GEntity) { // XXX GContainerEntityNode ?
    content = anEntity.toContent
  }

  final def addReference(aReference: String): TreeNode_TYPE = {
    require (aReference != null && aReference.length > 0)
    setContent(UPathString.getLastComponent(aReference), container.entityContext.makeReferenceContent(aReference))
  }
}
