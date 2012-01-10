package org.goldenport.sdoc.block

import scala.xml._

/*
 * derived from SBOrderedList.java since Sep. 22, 2006
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SBOrderedList extends SBlock {
}

object SBOrderedListXml {
  def unapply(xnode: Node): Option[SBOrderedList] = {
    if (xnode.label == "ol") Some(SBOrderedList())
    else None
  }
}

object SBOrderedListString {
  def unapply(string: String): Option[(SBOrderedList, Option[String], Option[String])] = None
}
