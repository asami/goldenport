package org.goldenport.sdoc.block

import scala.xml._

/*
 * derived from SBDefinitionTerm.java since Apr. 6, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SBDefinitionTerm extends SBlock {
}

object SBDefinitionTermXml {
  def unapply(xnode: Node): Option[SBDefinitionTerm] = {
    if (xnode.label == "dt") Some(SBDefinitionTerm())
    else None
  }
}

object SBDefinitionTermString {
  def unapply(string: String): Option[(SBDefinitionTerm, Option[String], Option[String])] = None
}
