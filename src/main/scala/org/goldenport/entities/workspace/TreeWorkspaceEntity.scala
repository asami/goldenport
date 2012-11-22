package org.goldenport.entities.workspace

import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.FileDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTreeBase

/*
 * @since   Aug. 19, 2008
 *  version Aug. 24, 2008
 * @version Nov. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class TreeWorkspaceEntity(aContext: GEntityContext) extends GTreeContainerEntityBase(null, aContext) {
  type DataSource_TYPE = FileDataSource
  override type TreeNode_TYPE = TreeWorkspaceNode

  commitMode = NoCommit

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  override protected def open_Entity_Create() {
    set_root(new TreeWorkspaceNode(null, this))
  }
}

class TreeWorkspaceEntityClass extends GEntityClass {
  type Instance_TYPE = TreeWorkspaceEntity
}
