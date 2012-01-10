package org.goldenport.sdoc.structure

import scala.xml._

/*
 * derived from SSSubSection.java since Apr. 6, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SSSubSection extends SSContentsStructure {
  override def xml_Element_Label = "subsection"
}

object SSSubSectionXml {
  def unapply(xnode: Node): Option[SSSubSection] = {
    if (xnode.label == "subsection") {
      val subsection = SSSubSection()
      subsection.putXmlAttributes(xnode.attributes)
      Some(subsection)
    } else None
  }
}

object SSSubSectionString {
  def unapply(string: String): Option[(SSSubSection, Option[String], Option[String])] = None
}
