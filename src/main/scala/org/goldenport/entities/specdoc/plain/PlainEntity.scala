package org.goldenport.entities.specdoc.plain

import scala.collection.mutable
import org.goldenport.value.GTreeNodeBase
import org.goldenport.sdoc.{SDoc, SText}
import org.goldenport.entities.specdoc.SDEntity

/*
 * derived from PlainSDBase.java since Mar. 6, 2007
 *
 * @since   Sep.  4, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class PlainEntity(aName: String) extends SDEntity(aName) {
  def this() = this(null)
}
