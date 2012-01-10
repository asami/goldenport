package org.goldenport.sdoc.block

import scala.xml._

/*
 * derived from SBDefinitionDescription.java since Apr. 6, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SBDefinitionDescription extends SBlock {
}

object SBDefinitionDescriptionXml {
  def unapply(xnode: Node): Option[SBDefinitionDescription] = {
    if (xnode.label == "dd") Some(SBDefinitionDescription())
    else None
  }
}

object SBDefinitionDescriptionString {
  def unapply(string: String): Option[(SBDefinitionDescription, Option[String], Option[String])] = None
}
