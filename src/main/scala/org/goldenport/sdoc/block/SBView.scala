package org.goldenport.sdoc.block

import scala.xml._

/*
 * Nov. 26, 2008
 * Nov. 26, 2008
 */
case class SBView extends SBlock {
  
}

object SBViewXml {
  def unapply(xnode: Node): Option[SBView] = {
    if (xnode.label == "view") Some(SBView())
    else None
  }
}

object SBViewString {
  def unapply(string: String): Option[(SBView, Option[String], Option[String])] = None
}
