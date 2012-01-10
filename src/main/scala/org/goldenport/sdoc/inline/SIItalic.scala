package org.goldenport.sdoc.inline

import scala.xml._
import org.goldenport.sdoc._

/*
 * derived from SLItalic.java since Sep. 20, 2006
 *
 * @since   Sep.  1, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SIItalic extends SInline {
  override def copy_Node(): SIItalic = {
    val italic = SIItalic()
    italic
  }
}

object SIItalicXml {
  val XmlTagName = "i"

  def unapply(xnode: Node): Option[SIItalic] = {
    if (xnode.label == XmlTagName) Some(SIItalic())
    else None
  }
}

object SIItalicString {
  val WikiSymbol = '_'

  def unapply(string: String): Option[(SIItalic, Option[String], Option[String])] = {
    if (string(0) != WikiSymbol) return None
    val (body: Option[String], tail: Option[String]) = USmartDoc.makeBody(string, WikiSymbol)
    val italic = new SIItalic()
    Some((italic, body, tail))
  }
}
