package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.FileLocator
import java.io._
import com.asamioffice.text.UPathString
import com.asamioffice.io.UFile

/*
 * @since   Aug.  6, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class FileDataSource(aFile: FileLocator, aContext: GEntityContext) extends GDataSource(aFile, aContext) with GContentDataSource {

  def this(filename: String, aContext: GEntityContext) = this(new FileLocator(filename), aContext)

  def this(aFile: File, aContext: GEntityContext) = this(new FileLocator(aFile), aContext)

  val file = aFile.file

  override def is_Exist: Boolean = {
    file.exists()
  }

  override def open_InputStream(): InputStream = {
    new FileInputStream(file)
  }

  override def open_OutputStream(): OutputStream = {
    UFile.createParentDirectory(file)
    new FileOutputStream(file)
  }

  def leafName: String = {
//    println("leafname -> " + UPathString.getLastComponent(file.toString().replace('\\', '/'))) 2008-10-31
    UPathString.getLastComponent(file.toString().replace('\\', '/'))
  }

  def children: List[FileDataSource] = {
    file.listFiles() match {
      case null => Nil
      case files => for (file <- files.toList) yield new FileDataSource(file, context)
    }
  }

  def child(aName: String): FileDataSource = {
    new FileDataSource(new File(file, aName), context)
  }
}
