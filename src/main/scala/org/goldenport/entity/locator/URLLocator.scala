package org.goldenport.entity.locator

import java.net.URL
import com.asamioffice.io.UURL

/*
 * Aug.  7, 2008
 * Aug. 14, 2008
 */
class URLLocator(aUrl: URL) extends GLocator{
  val url = aUrl
  val uri = url.toURI()
  override def getUrl() = Some(aUrl)
  override def getFile() = UURL.getActiveFile(url) match {
    case null => None
    case file => Some(file)
  }

  def this(aUrl: String) = this(new URL(aUrl))
}
