package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * Oct.  1, 2008
 * Oct. 18, 2008
 */
case class SBTableData extends SBlock {
}

object SBTableDataXml {
  def unapply(xnode: Node): Option[SBTableData] = {
    if (xnode.label == "td") Some(SBTableData())
    else None
  }
}
