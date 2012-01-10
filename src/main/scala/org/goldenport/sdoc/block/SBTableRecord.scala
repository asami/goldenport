package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * Oct.  1, 2008
 * Oct. 18, 2008
 */
case class SBTableRecord extends SBlock {
}

object SBTableRecordXml {
  def unapply(xnode: Node): Option[SBTableRecord] = {
    if (xnode.label == "tr") Some(SBTableRecord())
    else None
  }
}
