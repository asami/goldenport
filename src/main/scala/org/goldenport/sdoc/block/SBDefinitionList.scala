package org.goldenport.sdoc.block

import scala.xml._

/*
 * derived from SBDefinitionList.java since Apr. 6, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SBDefinitionList extends SBlock {
}

object SBDefinitionListXml {
  def unapply(xnode: Node): Option[SBDefinitionList] = {
    if (xnode.label == "dl") Some(SBDefinitionList())
    else None
  }
}

object SBDefinitionListString {
  def unapply(string: String): Option[(SBDefinitionList, Option[String], Option[String])] = None
}
