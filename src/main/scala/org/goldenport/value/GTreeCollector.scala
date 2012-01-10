package org.goldenport.value

import scala.collection.mutable.ListBuffer

/*
 * @since   Sep.  6, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
class GTreeCollector[E](val filter: GTreeNode[E] => Boolean) extends GTreeVisitor[E] {
  val collection = new ListBuffer[GTreeNode[E]]
  override def startEnter(aNode: GTreeNode[E]) {
    if (filter(aNode)) collection += aNode
  }

  def result: List[GTreeNode[E]] = collection.toList
}
