package org.goldenport.sdoc.block

import scala.xml._

/*
 * derived from SBDivision.java since Sep. 17, 2006
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SBDivision extends SBlock {
}

object SBDivisionXml {
  def unapply(xnode: Node): Option[SBDivision] = {
    if (xnode.label == "div") Some(SBDivision())
    else None
  }
}

object SBDivisionString {
  def unapply(string: String): Option[(SBDivision, Option[String], Option[String])] = None
}
