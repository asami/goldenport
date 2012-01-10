package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * Oct.  1, 2008
 * Oct. 18, 2008
 */
case class SBTableHeader extends SBlock {
}

object SBTableHeaderXml {
  def unapply(xnode: Node): Option[SBTableHeader] = {
    if (xnode.label == "th") Some(SBTableHeader())
    else None
  }
}
