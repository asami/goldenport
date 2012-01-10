package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity._
import org.goldenport.entity.locator.ContentLocator
import org.goldenport.entity.content.GContent

/*
 * @since   Aug. 16, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class EntityDataSource(aEntity: GEntity, aContext: GEntityContext) extends GDataSource(aEntity.locator, aContext) {
  val ds_entity = aEntity

  override def write_OutputStream(aOut: OutputStream): Boolean = {
    ds_entity.write(aOut)
    true
  }
}
