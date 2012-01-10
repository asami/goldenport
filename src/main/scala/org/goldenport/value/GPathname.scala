package org.goldenport.value

import scala.collection.mutable.ArrayBuffer

/*
 * Jul. 28, 2008
 * Aug.  4, 2008
 */
class GPathname(pathname: String) {
  private val is_absolute = pathname.startsWith("/")
  private val is_container = pathname.endsWith("/")
  var pn = if (is_absolute) pathname.substring(1) else pathname
  pn = if (is_container) pathname.substring(0, pathname.length - 1) else pn
  private val comps = new ArrayBuffer[String]
  comps ++= pn.split("[/]")

  def length: Int = comps.length
  def isAbsolute: Boolean = is_absolute
  def components: List[String] = comps.toList
}
