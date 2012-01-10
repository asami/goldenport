package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * Oct.  1, 2008
 * Oct. 18, 2008
 */
case class SBTableBody extends SBlock {
}

object SBTableBodyXml {
  def unapply(xnode: Node): Option[SBTableBody] = {
    if (xnode.label == "tbody") Some(SBTableBody())
    else None
  }
}
