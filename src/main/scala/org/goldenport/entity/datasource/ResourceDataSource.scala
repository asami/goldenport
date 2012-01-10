package org.goldenport.entity.datasource

import org.goldenport.entity.locator.ResourceLocator
import java.io._
import java.net.URL
import org.goldenport.entity.GEntityContext

/*
 * @since   Aug.  8, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class ResourceDataSource(aResource: ResourceLocator, aContext: GEntityContext) extends GDataSource(aResource, aContext) with GContentDataSource {
  private val resource_locator = aResource

  def this(aResourceName: String, aContext: GEntityContext) = this(new ResourceLocator(aResourceName), aContext)

  override protected def is_Exist: Boolean = {
    context.lookupResource(resource_locator) != null
  }

  override def open_InputStream(): InputStream = {
    val url = get_url
    url.openStream()
  }

  override def getUrl(): Option[URL] = {
    Some(get_url)
  }

  private def get_url: URL = {
    val url = context.lookupResource(resource_locator)
    if (url == null) {
      throw new IOException("Invalid url = " + url + ", Resource = " + resource_locator.resourceName)
    }
    url
  }
}
