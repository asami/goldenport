package org.goldenport.entity.content

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.GDataSource

/*
 * @since   Aug. 20, 2008
 * @version Jul. 15, 2010
 * @version Nov. 13, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class GDataSourceContent(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GContent(aContext) {
  val inputDataSource: GDataSource = aIn
  val outputDataSource: GDataSource = aOut

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)

  override def open_InputStream(): InputStream = {
    inputDataSource.openInputStream()
  }

  override def open_Reader(): Reader = {
    inputDataSource.openReader()
  }

  override def write_OutputStream(aOut: OutputStream): Boolean = {
    inputDataSource.write(aOut)
    true
  }

  def simpleName: String = inputDataSource.simpleName
  def suffix: Option[String] = inputDataSource.getSuffix()

  def mimeType: Option[String] = inputDataSource.mimeType
}
