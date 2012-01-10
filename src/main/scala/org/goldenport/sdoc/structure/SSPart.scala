package org.goldenport.sdoc.structure

import scala.xml._

/*
 * Sep. 29, 2008
 * Oct. 18, 2008
 */
case class SSPart extends SSContentsStructure {
  override def xml_Element_Label = "part"
}

object SSPartXml {
  def unapply(xnode: Node): Option[SSPart] = {
    if (xnode.label == "part") {
      val part = SSPart()
      part.putXmlAttributes(xnode.attributes)
      Some(part)
    } else None
  }
}

object SSPartString {
  def unapply(string: String): Option[(SSPart, Option[String], Option[String])] = None
}
