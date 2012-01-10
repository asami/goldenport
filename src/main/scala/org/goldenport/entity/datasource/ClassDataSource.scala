package org.goldenport.entity.datasource

import org.goldenport.dsl.specdoc.Entity
import org.goldenport.entity.GEntityContext

/*
 * @since   Sep.  5, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class ClassDataSource[E](aContext: GEntityContext)(classes: Class[E]*)  extends GDataSource(aContext) with GContentDataSource {
}
