package org.goldenport.entities.specdoc.scaladsl

import org.goldenport.dsl.specdoc.Entity
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource

/*
 * Sep.  5, 2008
 * Sep.  6, 2008
 */
class EntityDataSource(aContext: GEntityContext)(val entities: Entity*)  extends GDataSource(aContext) {
  override def is_Exist = true
//  final def entities: List[Entity] = error("XXX")
}
