package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * Oct.  1, 2008
 * Oct. 18, 2008
 */
case class SBTableFoot extends SBlock {
}

object SBTableFootXml {
  def unapply(xnode: Node): Option[SBTableFoot] = {
    if (xnode.label == "tfoot") Some(SBTableFoot())
    else None
  }
}
