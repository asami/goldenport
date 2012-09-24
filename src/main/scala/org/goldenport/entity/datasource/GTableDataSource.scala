package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.GLocator

/*
 * @since   Aug.  9, 2008
 *  version Sep.  6, 2008
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GTableDataSource(aLocator: GLocator, aContext: GEntityContext) extends GDataSource(aContext, aLocator) {
  override def is_Exist: Boolean = false
}
