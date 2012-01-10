package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.sdoc._

/*
 * derived from SBListItem.java since Sep. 22, 2006
 *
 * @since   Sep.  6, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SBListItem(theChildren: SDoc*) extends SBlock(theChildren: _*) {
}

object SBListItemXml {
  def unapply(xnode: Node): Option[SBListItem] = {
    if (xnode.label == "li") Some(SBListItem())
    else None
  }
}

object SBListItemString {
  def unapply(string: String): Option[(SBListItem, Option[String], Option[String])] = None
}
