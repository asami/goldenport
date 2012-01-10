package org.goldenport.sdoc.attribute

import scala.xml._
import org.goldenport.sdoc._

/*
 * @since   Sep.  3, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SATitle(theChildren: SDoc*) extends SAttribute(theChildren: _*) {
  override def xml_Element_Label = "title"
}

object SATitleXml {
  def unapply(xnode: Node): Option[SATitle] = {
    if (xnode.label == "title") Some(SATitle())
    else None
  }
}

object SATitleString {
  def unapply(string: String): Option[(SATitle, Option[String], Option[String])] = None
}
