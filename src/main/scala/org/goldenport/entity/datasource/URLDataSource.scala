package org.goldenport.entity.datasource

import java.net.URL
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.URLLocator

/*
 * @since   Aug.  9, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class URLDataSource(aUrl: URLLocator, aContext: GEntityContext) extends GDataSource(aContext, aUrl) with GContentDataSource {

  def this(aUrl: URL, aContext: GEntityContext) = this(new URLLocator(aUrl), aContext)
  def this(aUrl: String, aContext: GEntityContext) = this(new URLLocator(aUrl), aContext)
}
