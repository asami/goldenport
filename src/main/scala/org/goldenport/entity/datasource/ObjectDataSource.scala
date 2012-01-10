package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.dsl.specdoc.Entity

/*
 * @since   Sep.  5, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class ObjectDataSource(aContext: GEntityContext)(objects: AnyRef*)  extends GDataSource(aContext) with GContentDataSource {
}
