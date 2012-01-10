package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity.locator.NameLocator
import org.goldenport.entity.GEntityContext

/*
 * @since   Oct.  2, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class AnyDataSource(val any: Any, aContext: GEntityContext) extends GDataSource(new NameLocator(any.toString), aContext) with GContentDataSource {
}
