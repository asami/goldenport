package org.goldenport.sdoc

/*
 * Sep.  4, 2008
 * Oct. 18, 2008
 */
case class SEmpty extends SDoc {
  override def isNil = true
  
  override def copy_Node(): SEmpty = SEmpty
}

object SEmpty extends SEmpty {
}
