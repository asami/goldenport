package org.goldenport.entities.view

import scala.collection.mutable.ArrayBuffer
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource

/*
 * @since   Aug. 26, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class TreeViewDataSource(aContext: GEntityContext)(theTrees: Seq[(GTreeContainerEntity, String, String, String)]) extends GDataSource(aContext) {
  def this(aTree: GTreeContainerEntity, aContext: GEntityContext) =
    this(aContext)(Array((aTree, null, null, null)))

  private val ds_trees = theTrees

  override def is_Exist = true

  final def trees: Seq[(String, GTreeContainerEntityNode)] = {
    val nodes = new ArrayBuffer[(String, GTreeContainerEntityNode)]
    for ((tree, sourcePathname, targetPathname, nameFilter) <- ds_trees) {
      val mayNode = get_node(tree, sourcePathname)
      if (mayNode.isDefined) {
	for (child <- get_children(mayNode.get, nameFilter)) {
	  nodes += ((make_pathname(targetPathname, child), child))
	}
      } else {
	// XXX
      }
    }
    nodes
  }

  private def make_pathname(targetPathname: String, child: GTreeContainerEntityNode): String = {
    if (targetPathname == null) child.name
    else targetPathname + "/" + child.name
  }

  private def get_node(aTree: GTreeContainerEntity, aPathname: String): Option[GTreeContainerEntityNode] = {
    if (aPathname == null) Some(aTree.root)
    else aTree.getNode(aPathname)
  }

  private def get_children(node: GTreeContainerEntityNode, nameFilter: String): Seq[GTreeContainerEntityNode] = {
    node.children
  }
}
