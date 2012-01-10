package org.goldenport.sdoc.structure

import scala.xml._

/*
 * derived from SSSection.java since Sep. 17, 2006
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SSSection extends SSContentsStructure {
  override def xml_Element_Label = "section"
}

object SSSectionXml {
  def unapply(xnode: Node): Option[SSSection] = {
    if (xnode.label == "section") {
      val section = SSSection()
      section.putXmlAttributes(xnode.attributes)
      Some(section)
    } else None
  }
}

object SSSectionString {
  def unapply(string: String): Option[(SSSection, Option[String], Option[String])] = None
}
