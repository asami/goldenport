package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.sdoc._

/*
 * derived from SBParagraph.java since Sep. 21, 2006
 *
 * @since   Sep.  6, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SBParagraph(theChildren: SDoc*) extends SBlock(theChildren: _*) {
}

object SBParagraphXml {
  def unapply(xnode: Node): Option[SBParagraph] = {
    if (xnode.label == "p") Some(SBParagraph())
    else None
  }
}

object SBParagraphString {
  def unapply(string: String): Option[(SBParagraph, Option[String], Option[String])] = None
}
