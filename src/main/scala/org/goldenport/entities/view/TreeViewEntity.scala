package org.goldenport.entities.view

import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.FileDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTreeBase

/*
 * @since   Aug. 26, 2008
 *  version Oct. 31, 2008
 * @version Nov. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class TreeViewEntity(aDataSource: TreeViewDataSource, aContext: GEntityContext) extends GTreeContainerEntityBase(aDataSource, aContext) {
  type DataSource_TYPE = TreeViewDataSource
  override type TreeNode_TYPE = TreeViewNode

  commitMode = NormalCommit

  def this(aEntity: GTreeContainerEntity, aContext: GEntityContext) =
    this(new TreeViewDataSource(aEntity, aContext), aContext)

  override def open_Entity_Update(aDataSource: DataSource_TYPE) {
    val root = new TreeViewNode(null, this)
    set_root(root)
    val trees = inputDataSource.trees
    for ((pathname, source) <- trees) {
//      println("pathname = " + pathname + ", source = " + source) 2008-10-31
      val node = root.setNode(pathname)
      node.source = source
    }
  }

/*
  override def open_Entity_Create() {
    val root = new TreeViewNode(null, null, this)
    set_root(root)
    val trees = inputDataSource.trees
    trees
  }
*/
}

class TreeViewEntityClass extends GEntityClass {
  type Instance_TYPE = TreeViewEntity
}
