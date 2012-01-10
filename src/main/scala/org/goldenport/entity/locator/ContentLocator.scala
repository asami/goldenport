package org.goldenport.entity.locator

import java.net.URI
import org.goldenport.entity.content.GContent

/*
 * Aug. 14, 2008
 * Aug. 17, 2008
 */
abstract class ContentLocator(aContent: GContent) extends GLocator {
  val uri = new URI("content:" + aContent)
  val content = aContent
}
