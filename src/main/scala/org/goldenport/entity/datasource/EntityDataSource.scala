package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity._
import org.goldenport.entity.locator.ContentLocator
import org.goldenport.entity.content.GContent

/*
 * @since   Aug. 16, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class EntityDataSource(aEntity: GEntity, aContext: GEntityContext) extends GDataSource(aContext, aEntity.locator) {
  val ds_entity = aEntity

  override def write_OutputStream(aOut: OutputStream): Boolean = {
    ds_entity.write(aOut)
    true
  }
}
