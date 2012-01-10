package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * Oct.  1, 2008
 * Oct. 18, 2008
 */
case class SBTableHead extends SBlock {
}

object SBTableHeadXml {
  def unapply(xnode: Node): Option[SBTableHead] = {
    if (xnode.label == "thead") Some(SBTableHead())
    else None
  }
}
