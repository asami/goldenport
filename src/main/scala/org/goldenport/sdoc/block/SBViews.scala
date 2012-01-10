package org.goldenport.sdoc.block

import scala.xml._

/*
 * Nov. 26, 2008
 * Nov. 27, 2008
 */
case class SBViews extends SBlock {
  final def views: Seq[SBView] = {
    children.filter(_.isInstanceOf[SBView]).map(_.asInstanceOf[SBView])
  }
}

object SBViewsXml {
  def unapply(xnode: Node): Option[SBViews] = {
    if (xnode.label == "views") Some(SBViews())
    else None
  }
}

object SBViewsString {
  def unapply(string: String): Option[(SBViews, Option[String], Option[String])] = None
}
