package org.goldenport.sdoc

import scala.xml._

/*
 * derived from SText.java since Sep. 19, 2006
 *
 * Sep.  1, 2008
 * Oct. 18, 2008
 */
case class SFragment(theChildren: SDoc*) extends SDoc(theChildren: _*) {
  override def copy_Node(): SFragment = SFragment()
}

object SFragmentXml {
  def unapply(xnode: Node): Option[SFragment] = {
    xnode.label match {
      case "fragment" => Some(SFragment())
      case "text" => Some(SFragment())
      case "t" => Some(SFragment())
      case _ => None
    }
  }
}
