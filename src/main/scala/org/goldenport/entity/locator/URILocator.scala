package org.goldenport.entity.locator

import java.net.URI
import com.asamioffice.io.UURL

/*
 * Aug.  7, 2008
 * Aug. 14, 2008
 */
class URILocator(aUri: URI) extends GLocator{
  val uri:URI = aUri
  override def getUrl() = Some(uri.toURL())
  override def getFile() = UURL.getActiveFile(uri) match {
    case null => None
    case file => Some(file)
  }

  def this(aUri: String) = this(new URI(aUri))
}
