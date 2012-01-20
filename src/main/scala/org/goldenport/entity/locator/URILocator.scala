package org.goldenport.entity.locator

import java.net.URI
import com.asamioffice.goldenport.io.UURL

/**
 * @since   Aug.  7, 2008
 * Aug. 14, 2008
 * @version Jan. 20, 2012
 * @author  ASAMI, Tomoharu
 */
class URILocator(aUri: URI) extends GLocator{
  val uri:URI = aUri
  override def getUrl() = Some(uri.toURL())
  override def getFile() = Option(UURL.getFileFromFileNameOrURLName(uri.toString()))

  def this(aUri: String) = this(new URI(aUri))
}
