package org.goldenport.entity

import org.goldenport.parameter.{GParameterRepository, NullParameterRepository}
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entities.view.TreeViewEntity

/*
 * Aug. 30, 2008
 * Nov.  3, 2008
 */
class SubEntitySpace(val parent: GEntitySpace, theParams: GParameterRepository) extends GEntitySpace(parent.containerContext, theParams) {
  val tree = new TreeViewEntity(parent.tree, context)

  def this(aParent: GEntitySpace) = this(aParent, NullParameterRepository)

  override def reconstitute_DataSource(aDataSource: GDataSource): Option[GEntity] = parent.reconstitute(aDataSource)

  override def reconstitute_Node(aNode: GContainerEntityNode): Option[GEntity] = parent.reconstitute(aNode)
}
