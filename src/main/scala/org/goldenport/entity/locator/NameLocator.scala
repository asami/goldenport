package org.goldenport.entity.locator

import java.net.URI

/*
 * Aug.  8, 2008
 * Aug. 14, 2008
 */
class NameLocator(aName: String) extends GLocator {
  val uri = new URI("name:" + aName)
  val name = aName
}
