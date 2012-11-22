package org.goldenport.entity.datasource

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.FileLocator

/*
 * @since   Aug. 13, 2008
 *  version Jul. 15, 2010
 *  version Sep. 24, 2012
 * @version Nov. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class ContentFileDataSource(aFile: FileLocator, aContext: GEntityContext) extends FileDataSource(aContext, aFile, None) {

  def this(filename: String, aContext: GEntityContext) = this(new FileLocator(filename), aContext)

  override def open_InputStream(): InputStream = {
    new FileInputStream(file)
  }

  override def open_OutputStream(mode: OutputMode): Option[OutputStream] = {
//    println("ContentFileDataSource#output_stream: " + file)
    Some(new FileOutputStream(file))
  }
}
