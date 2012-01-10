package org.goldenport.sdoc.inline

import scala.xml._
import org.goldenport.sdoc._

/*
 * derived from SLBold.java since Sep. 20, 2006
 *
 * @since   Sep.  1, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SIBold extends SInline {
  override def copy_Node(): SIBold = {
    val bold = SIBold()
    bold
  }
}

object SIBoldXml {
  val XmlTagName = "b"

  def unapply(xnode: Node): Option[SIBold] = {
    if (xnode.label == XmlTagName) Some(SIBold())
    else None
  }
}

object SIBoldString {
  val WikiSymbol = '*'

  def unapply(string: String): Option[(SIBold, Option[String], Option[String])] = {
    if (string(0) != WikiSymbol) return None
    val (body: Option[String], tail: Option[String]) = USmartDoc.makeBody(string, WikiSymbol)
    val bold = new SIBold()
    Some((bold, body, tail))
  }
}
