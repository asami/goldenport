package org.goldenport.entities.zip

import java.io.{File, InputStream, OutputStream}
import java.util.zip._
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, FileDataSource, BinaryDataSource, NullDataSource}
import org.goldenport.entity.content.{GContent, BinaryContent}
import org.goldenport.entities.workspace.TreeWorkspaceNode
import com.asamioffice.goldenport.io.ContextOutputStream

/*
 * derived from ZipModel.java since Aug. 8, 2004
 *
 * @since   Feb.  1, 2009
 * @version Nov. 13, 2011
 * @author  ASAMI, Tomoharu
 */
class ZipEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeContainerEntityBase(aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource
  override type TreeNode_TYPE = GTreeContainerEntityNode

  private var _zip_file: ZipFile = _
  
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aFile: File, aContext: GEntityContext) = this(new FileDataSource(aFile, aContext), aContext)
//  def this(aFile: File, aContext: GEntityContext) = this(new BinaryDataSource(new java.io.FileInputStream(aFile), aContext), aContext)

  def this(aIn: InputStream, aContext: GEntityContext) = this(BinaryDataSource.createInputStream(aIn, aContext), aContext)
  def this(aContext: GEntityContext) = this(NullDataSource, aContext)

  override protected def open_Entity_Create() {
    set_root(new TreeWorkspaceNode(null, this))
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
    set_root(new TreeWorkspaceNode(null, this))
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    open_entity(aDataSource)
  }

  private def open_entity(aDataSource: GDataSource) {
    set_root(new TreeWorkspaceNode(null, this))

    def build_file {
      val entries = _zip_file.entries()
      while (entries.hasMoreElements()) {
        val entry = entries.nextElement()
        setContent(entry.getName, new ZipEntryContent(entry, _zip_file, entityContext))
      }
    }

    def build_stream {
      val in = new ZipInputStream(aDataSource.openInputStream)
      try {
        var entry: ZipEntry = in.getNextEntry()
        while (entry != null) {
          setContent(entry.getName, new BinaryContent(in, entityContext))
          entry = in.getNextEntry()
        }
      } finally {
        in.close()
      }
    }

    _zip_file = aDataSource.getFile() match {
      case Some(file) => new ZipFile(file)
      case None => null
    }
    if (_zip_file != null) {
      build_file
    } else {
      build_stream
    }
  }

  override protected def entity_Suffix = Some("zip")

  override protected def write_Content(anOut: OutputStream): Unit = {
//    val zip = new ZipOutputStream(new ContextOutputStream(anOut))
    val zip = new ZipOutputStream(anOut)

    def write_entry(aNode: GTreeContainerEntityNode) {
      def is_container = {
//        println("aNode.content = " + aNode.content + "(" + aNode.pathname + ")") 2009-03-01
        aNode.content == null
      }

      def zip_entry_name = aNode.pathname.substring(1)

      if (is_container) {
        val entry = new ZipEntry(zip_entry_name + "/")
//        println("container = " + entry.getName)
        zip.putNextEntry(entry)
        for (child <- aNode.children) {
          write_entry(child)
        }
      } else {
        val entry = new ZipEntry(zip_entry_name)
        val content = aNode.content
        entry.setSize(content.getSize)
//        println("leaf = " + entry.getName + ", size = " + entry.getSize + ", content = " + content)
        zip.putNextEntry(entry)
        content.write(zip)
      }
    }

    for (child <- root.children) {
      write_entry(child)
    }
    zip.flush()
    zip.close()
  }
}

class ZipEntityClass extends GEntityClass {
  type Instance_TYPE = ZipEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "zip"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new ZipEntity(aDataSource, aContext))
}
