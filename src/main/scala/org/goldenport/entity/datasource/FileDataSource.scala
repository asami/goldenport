package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.FileLocator
import java.io._
import java.net.URI
import org.goldenport.util.UriAux
import com.asamioffice.goldenport.text.UPathString
import com.asamioffice.goldenport.io.UFile

/*
 * @since   Aug.  6, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class FileDataSource(aContext: GEntityContext, aFile: FileLocator, aux: Option[UriAux]) extends GDataSource(aContext, aFile, aux) with GContentDataSource {
  def this(filename: String, aContext: GEntityContext) = this(aContext, new FileLocator(filename), None)
  def this(aFile: File, aContext: GEntityContext) = this(aContext, new FileLocator(aFile), None)

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

object FileDataSource {
  def fromUri(context: GEntityContext, uri: URI): FileDataSource = {
    val f = new FileLocator(uri.getSchemeSpecificPart)
    val aux = UriAux(uri)
    new FileDataSource(context, f, Some(aux))
  }
}
