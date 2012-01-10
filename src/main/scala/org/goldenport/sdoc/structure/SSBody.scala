package org.goldenport.sdoc.structure

import scala.xml._

/*
 * derived from SSBody.java since Jan. 15, 2007
 *
 * Sep.  2, 2008
 * Oct. 18, 2008
 */
case class SSBody extends SStructure {
  override def xml_Element_Label = "body"
}

object SSBodyXml {
  def unapply(xnode: Node): Option[SSBody] = {
    if (xnode.label == "body") Some(SSBody())
    else None
  }
}
