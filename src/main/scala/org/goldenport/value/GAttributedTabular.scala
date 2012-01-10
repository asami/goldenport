package org.goldenport.value

/*
 * @since   Sep.  8, 2008
 * @version Mar. 15, 2009
 * @version Dec.  1, 2011
 * @author  ASAMI, Tomoharu
 */
trait GAttributedTabular[E] extends GTabular[E] {
  def getAttribute(x: Int, y: Int): GTabularAttribute
  def setAttribute(x: Int, y: Int, attr: GTabularAttribute): Unit
  def putAllWithAttributes(aSrc: GAttributedTabular[E]): Unit
}

class GTabularAttribute {
  var rowspan: Int = 1
  var colspan: Int = 1
}
