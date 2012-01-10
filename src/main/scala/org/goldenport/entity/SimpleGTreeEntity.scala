package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTreeBase
import org.goldenport.value.GTreeNode

/*
 * Jul. 26, 2008
 * Sep.  1, 2008
 */
class SimpleGTreeEntity[E](aDataSource: GDataSource, aContext: GEntityContext) extends GTreeEntity[E](aDataSource, aContext) with GTreeBase[E] {
  type TreeNode_TYPE = GTreeNode[E]
  type DataSource_TYPE = GDataSource

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  override protected def open_Entity_Update(aDataSource: GDataSource) {
  }
}
