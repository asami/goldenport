package org.goldenport.entity.datasource

import java.net.URI
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.URILocator

/*
 * @since   Aug.  9, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class URIDataSource(aUri: URILocator, aContext: GEntityContext) extends GDataSource(aContext, aUri) with GContentDataSource {

  def this(aUri: URI, aContext: GEntityContext) = this(new URILocator(aUri), aContext)
  def this(aUri: String, aContext: GEntityContext) = this(new URILocator(aUri), aContext)
}
