package org.goldenport.entity.locator

import java.net.URI

/*
 * Sep.  6, 2008
 * Sep.  6, 2008
 */
class NullLocator extends GLocator {
  val uri = new URI("null:null")
}

object NullLocator extends NullLocator {
}
