package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.GLocator

/*
 * Aug.  9, 2008
 * Sep.  6, 2008
 */
abstract class GTableDataSource(aLocator: GLocator, aContext: GEntityContext) extends GDataSource(aLocator, aContext) {
  override def is_Exist: Boolean = false
}
