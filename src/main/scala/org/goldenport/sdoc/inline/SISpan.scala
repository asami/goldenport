package org.goldenport.sdoc.inline

import scala.xml._
import org.goldenport.sdoc._

/*
 * derived from SLSpan.java since Sep. 17, 2006
 *
 * @since   Nov.  9, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SISpan(theChildren: SDoc*) extends SInline(theChildren: _*) {
  override def copy_Node(): SISpan = {
    val span = SISpan()
    span
  }
}

object SISpanXml {
  val XmlTagName = "span"

  def unapply(xnode: Node): Option[SISpan] = {
    if (xnode.label == XmlTagName) Some(SISpan())
    else None
  }
}
