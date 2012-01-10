package org.goldenport.sdoc.structure

import scala.collection.mutable.ListBuffer
import scala.xml._
import org.goldenport.sdoc.attribute.SATitle

/*
 * derived from SSHead.java since Jan. 17, 2007
 *
 * Sep.  2, 2008
 * Oct. 18, 2008
 */
case class SSHead extends SStructure {
  override def xml_Element_Label = "head"
}

object SSHeadXml {
  def unapply(xnode: Node): Option[SSHead] = {
    if (xnode.label == "head") Some(SSHead())
    else None
  }
}
