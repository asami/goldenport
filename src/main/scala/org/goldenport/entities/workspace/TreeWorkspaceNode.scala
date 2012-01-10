package org.goldenport.entities.workspace

import org.goldenport.entity._
import org.goldenport.entity.datasource._
import org.goldenport.entity.content.GContent

/*
 * Aug. 19, 2008
 * Oct.  7, 2008
 */
class TreeWorkspaceNode(aName: String, aEntity: GTreeContainerEntity) extends GTreeContainerEntityNode(aName, aEntity) {
  type DataSource_TYPE = WorkspaceDataSource
//  type ContainerEntity_TYPE = TreeWorkspaceEntity
  type ContainerEntity_TYPE = GTreeContainerEntity
  type TreeNode_TYPE = TreeWorkspaceNode

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TreeWorkspaceNode(aName, container)
  }
}
