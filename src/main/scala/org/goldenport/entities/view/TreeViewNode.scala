package org.goldenport.entities.view

import org.goldenport.entity._
import org.goldenport.entity.datasource._
import org.goldenport.entity.content.GContent

/*
 * Aug. 26, 2008
 * Aug. 28, 2008
 */
class TreeViewNode(aName: String, aEntity: TreeViewEntity) extends GTreeContainerEntityNode(aName, aEntity) {
  type ContainerEntity_TYPE = TreeViewEntity
  type TreeNode_TYPE = TreeViewNode
  /*
   * In case of newly added node, source is null.
   */
  var source: GContainerEntityNode = null

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TreeViewNode(aName, container)
  }

  override def get_Input_DataSource(): Option[GDataSource] = {
    if (source == null) None
    else source.getInputDataSource()
  }

  override def get_Output_DataSource(): Option[GDataSource] = {
    if (source == null) None
    else source.getOutputDataSource()
  }

  override def commit_Dirty_Node(): Unit = {
    if (source == null) return
    source.content = content
  }

  override def load_Content(): GContent = {
    if (source == null) return null
    source.content
  }

  override def load_Children: Seq[TreeViewNode] = {
    if (source == null) return Nil
    if (!source.isInstanceOf[GTreeContainerEntityNode]) return Nil
    for (child <- source.asInstanceOf[GTreeContainerEntityNode].children) yield {
      val node = new TreeViewNode(child.name, container)
      node.source = child
      node
    }
  }
}
