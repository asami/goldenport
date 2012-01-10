package org.goldenport.sdoc.structure

import scala.xml._

/*
 * derived from SSChapter.java since Apr. 7, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class SSChapter extends SSContentsStructure {
  override def xml_Element_Label = "chapter"
}

object SSChapterXml {
  def unapply(xnode: Node): Option[SSChapter] = {
    if (xnode.label == "chapter") {
      val chapter = SSChapter()
      chapter.putXmlAttributes(xnode.attributes)
      Some(chapter)
    } else None
  }
}

object SSChapterString {
  def unapply(string: String): Option[(SSChapter, Option[String], Option[String])] = None
}
