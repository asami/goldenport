package org.goldenport.value

/*
 * @since   Aug. 17, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
class FunctionVisitor[E](aFunction: E => Unit) extends GTreeVisitor[E] {
  override def startEnter(node: GTreeNode[E]): Unit = {
    if (node.content != null) aFunction(node.content)
  }
}
