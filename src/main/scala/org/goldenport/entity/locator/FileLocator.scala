package org.goldenport.entity.locator

import java.io.File

/*
 * @since   Aug.  7, 2008
 *          Aug. 14, 2008
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class FileLocator(aFile: File) extends GLocator {
  val file = aFile
  override def getFile() = Some(file)
  override def getUrl = Some(file.toURI.toURL())
  def uri = file.toURI()

  def this(filename: String) = this(new File(filename))
}
