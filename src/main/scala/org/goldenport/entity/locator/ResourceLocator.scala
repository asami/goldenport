package org.goldenport.entity.locator

import java.net.URI

/*
 * Aug.  7, 2008
 * Aug. 14, 2008
 */
class ResourceLocator(aResourceName: String) extends GLocator {
  val uri = new URI("resource:" + aResourceName)
  val resourceName = aResourceName
}
