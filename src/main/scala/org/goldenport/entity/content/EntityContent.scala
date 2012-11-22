package org.goldenport.entity.content

import java.io._
import org.goldenport.entity.GEntity
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.GDataSource

/*
 * @since   Aug. 10, 2008
 *  version Jul. 29, 2010
 *  version Dec. 14, 2011
 * @version Nov. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class EntityContent(aEntity: GEntity, aContext: GEntityContext) extends GContent(aContext) {
  val entity: GEntity = aEntity
  lazy private val _binary: BinaryContent = entity.toBinaryContent

  override def isCommitable = entity.isCommitable

  override def get_Suffix: Option[String] = entity.getSuffix()

  override def open_Content() {
    entity.open()
  }

  override def close_Content() {
    entity.close()
  }

  override protected def get_Size: Option[Long] = Some(_binary.size)  
  override protected def get_Binary(): Option[Array[Byte]] = None
  override protected def open_InputStream(): InputStream = _binary.openInputStream()
  override protected def open_Reader(): Reader = _binary.openReader()

  override def write_OutputStream(aOut: OutputStream): Boolean = {
    entity.write(aOut)
    true
  }
}
