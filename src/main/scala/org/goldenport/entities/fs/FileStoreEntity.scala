package org.goldenport.entities.fs

import java.io.File
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.FileDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTreeBase

/*
 * Aug.  9, 2008
 * Aug. 27, 2008
 */
class FileStoreEntity(aDataSource: FileDataSource, aContext: GEntityContext) extends GTreeContainerEntityBase(aDataSource, aContext) {
  override type DataSource_TYPE = FileDataSource
  override type TreeNode_TYPE = FileStoreNode
  override def is_Special_Commit = true

  def this(aFile: File, aContext: GEntityContext) = this(new FileDataSource(aFile, aContext), aContext)

  def this(aFile: String, aContext: GEntityContext) = this(new FileDataSource(aFile, aContext), aContext)

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  override protected def open_Entity_Create(aDataSource: FileDataSource) {
    open_entity(aDataSource)
  }

  override protected def open_Entity_Update(aDataSource: FileDataSource) {
    open_entity(aDataSource)
  }

  private def open_entity(aDataSource: FileDataSource) {
    set_root(new FileStoreNode(null, aDataSource, this))
  }
}

class FileStoreEntityClass extends GEntityClass {
  type Instance_TYPE = FileStoreEntity

  override def accept_DataSource(aDataSource: GDataSource): Boolean = {
    aDataSource.getFile().isDefined
  }

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = {
    if (aDataSource.isInstanceOf[FileDataSource])
      Some(new FileStoreEntity(aDataSource.asInstanceOf[FileDataSource], aContext))
    else
      Some(new FileStoreEntity(aDataSource.getFile().get, aContext))
  }
}
