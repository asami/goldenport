package org.goldenport.entity.locator

import java.net.URI
import org.goldenport.entity.GEntity

/*
 * Aug. 17, 2008
 * Aug. 17, 2008
 */
abstract class EntityLocator(aEntity: GEntity) extends GLocator {
  val uri = new URI("entity:" + aEntity)
  val entity = aEntity
}
