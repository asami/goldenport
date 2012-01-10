package org.goldenport.sdoc.structure

import scala.xml._

/*
 * derived from SSSubSubSection.java since Apr. 6, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SSSubSubSection extends SSContentsStructure {
  override def xml_Element_Label = "subsubsection"
}

object SSSubSubSectionXml {
  def unapply(xnode: Node): Option[SSSubSubSection] = {
    if (xnode.label == "subsubsection") {
      val subusubsection = SSSubSubSection()
      subusubsection.putXmlAttributes(xnode.attributes)
      Some(subusubsection)
    } else None
  }
}

object SSSubSubSectionString {
  def unapply(string: String): Option[(SSSubSubSection, Option[String], Option[String])] = None
}
