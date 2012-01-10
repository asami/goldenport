package org.goldenport.sdoc.block

import scala.xml._

/*
 * derived from SBUnorderedList.java since Sep. 22, 2006
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SBUnorderedList extends SBlock {
}

object SBUnorderedListXml {
  def unapply(xnode: Node): Option[SBUnorderedList] = {
    if (xnode.label == "ul") Some(SBUnorderedList())
    else None
  }
}

object SBUnorderedListString {
  def unapply(string: String): Option[(SBUnorderedList, Option[String], Option[String])] = None
}
