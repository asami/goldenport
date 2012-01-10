package org.goldenport.entities.workspace

import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.FileDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTreeBase

/*
 * Aug. 19, 2008
 * Aug. 24, 2008
 */
class TreeWorkspaceEntity(aContext: GEntityContext) extends GTreeContainerEntityBase(null, aContext) {
  type DataSource_TYPE = FileDataSource
  override type TreeNode_TYPE = TreeWorkspaceNode
  override def is_Commitable = false

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  override protected def open_Entity_Create() {
    set_root(new TreeWorkspaceNode(null, this))
  }
}

class TreeWorkspaceEntityClass extends GEntityClass {
  type Instance_TYPE = TreeWorkspaceEntity
}
