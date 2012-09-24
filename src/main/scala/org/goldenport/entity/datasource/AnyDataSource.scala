package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity.locator.NameLocator
import org.goldenport.entity.GEntityContext

/*
 * @since   Oct.  2, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class AnyDataSource(val any: Any, aContext: GEntityContext) extends GDataSource(aContext, new NameLocator(any.toString)) with GContentDataSource {
}
