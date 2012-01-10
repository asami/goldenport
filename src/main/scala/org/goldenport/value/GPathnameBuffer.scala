package org.goldenport.value

import scala.collection.mutable.ArrayBuffer

/*
 * Aug.  4, 2008
 * Oct. 31, 2008
 */
class GPathnameBuffer {
  var absolute = false
  var container = false
  private val comps = new ArrayBuffer[String]

  def addLeaf(name: String) {
    comps += name
  }

  def addContainer(name: String) {
    comps.insert(0, name)
  }

  override def toString: String = {
    comps.mkString(if (absolute) "/" else "", "/", if (container) "/" else "")
  }
}
