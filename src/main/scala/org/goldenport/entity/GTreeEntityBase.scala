package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.content.EntityContent
import org.goldenport.value.GTree
import org.goldenport.value.GTreeBase
import org.goldenport.value.GTreeNode

/*
 * @since   Sep.  2, 2008
 * @version Sep. 14, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GTreeEntityBase[E](aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeEntity[E](aIn, aOut, aContext) with GTreeBase[E] {
  def this(aContext: GEntityContext) = this(null, null, aContext)

  final override def open_Pre_Condition() {
    // XXX start/finish initialization
  }

  final override def open_Post_Condition() {
    assert(root != null)
    clearModified()
  }

  final override def set_Modified() {
    set_dirty()
  }

  final override def copy_Node(aSource: GTreeNode[E], aTarget: GTreeNode[E]) {
    error("not implemented yet.")
  }

  final override def open_Source(aSource: GTree[E]) {
    aSource.asInstanceOf[GTreeEntity[E]].open()
  }

  final override def close_Source(aSource: GTree[E]) {
    aSource.asInstanceOf[GTreeEntity[E]].close()
  }
}
