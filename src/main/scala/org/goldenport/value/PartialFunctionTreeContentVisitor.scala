package org.goldenport.value

/**
 * @since   May.  3, 2012
 * @version May.  3, 2012
 * @author  ASAMI, Tomoharu
 */
class PartialFunctionTreeContentVisitor[E](val pf: PartialFunction[E, _]) extends GTreeVisitor[E] {
  override def startEnter(node: GTreeNode[E]): Unit = {
    val c = node.content
    if (pf.isDefinedAt(c)) {
      pf(c)
    }
  }
}
