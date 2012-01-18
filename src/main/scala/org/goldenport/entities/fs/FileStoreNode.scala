package org.goldenport.entities.fs

import java.io.File
import org.goldenport.entity._
import org.goldenport.entity.datasource._
import org.goldenport.entity.content.GContent
import org.goldenport.entity.content.FileContent
import org.goldenport.value.GTreeNodeStructureBase
import com.asamioffice.goldenport.io.UFile

/*
 * Aug. 10, 2008
 * Oct.  7, 2008
 */
class FileStoreNode(aName: String, aDataSource: FileDataSource, aEntity: GTreeContainerEntity) extends GTreeContainerEntityNode(aName, aEntity) {
  type DataSource_TYPE = FileDataSource
//  type ContainerEntity_TYPE = FileStoreEntity
  type ContainerEntity_TYPE = GTreeContainerEntity
  type TreeNode_TYPE = FileStoreNode

  set_dataSource(aDataSource)
  val file = aDataSource.file

  protected final def ensure_file() {
  }

  private def fs_dataSource: FileDataSource = {
    get_dataSource() match {
      case None => error("empty")
      case Some(ds) => ds.asInstanceOf[FileDataSource]
    }
  }

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new FileStoreNode(aName, fs_dataSource.child(aName), container)
  }

  override protected def load_Content(): GContent = {
    ensure_file()
    if (!file.exists()) return null
    if (file.isDirectory()) return null
    new FileContent(file, container.entityContext)
  }

  override protected def load_Children: List[FileStoreNode] = {
    ensure_file()
    for (ds <- fs_dataSource.children) yield
      new FileStoreNode(ds.leafName, ds, container)
  }

  override protected def commit_Dirty_Node() {
    UFile.createDirectory(file)
  }
}

/*
class ContentFileStoreNode(aName: String, aFile: FileDataSource, aEntity: FileStoreEntity) extends FileStoreNode(aName, aFile, aEntity) {
  println("ContentFile : " + aName)
  override protected def load_Content(): GContent = {
    ensure_file()
    if (!file.exists()) return null
    if (file.isDirectory()) return null
    new FileContent(file, container.entityContext)
  }
}

class ContainerFileStoreNode(aName: String, aDirectory: FileDataSource, aEntity: FileStoreEntity) extends FileStoreNode(aName, aDirectory, aEntity) {
  println("ContainerFile : " + aName)
  override protected def load_Children: List[FileStoreNode] = {
    println("load_Children: " + datasource)
    println("  children   : " + datasource.children)
    ensure_file()
    for (ds <- datasource.children) yield
      if (ds.file.isDirectory())
	new ContainerFileStoreNode(ds.leafName, ds, container)
      else
	new ContentFileStoreNode(ds.leafName, ds, container)
  }
}

class RootFileStoreNode(aDirectory: FileDataSource, aEntity: FileStoreEntity) extends ContainerFileStoreNode(null, aDirectory, aEntity) {
}
*/
