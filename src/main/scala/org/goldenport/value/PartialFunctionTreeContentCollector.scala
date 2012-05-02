package org.goldenport.value

import scala.collection.mutable.ListBuffer

/**
 * @since   May.  3, 2012
 * @version May.  3, 2012
 * @author  ASAMI, Tomoharu
 */
class PartialFunctionTreeContentCollector[E, R](val pf: PartialFunction[E, R]) extends GTreeVisitor[E] {
  val collection = new ListBuffer[R]
  override def startEnter(aNode: GTreeNode[E]) {
    val c = aNode.content
    if (pf.isDefinedAt(c)) {
      collection += pf(c)
    }
  }

  def result: List[R] = collection.toList
}
