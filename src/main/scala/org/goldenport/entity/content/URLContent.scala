package org.goldenport.entity.content

import java.net.URL
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.URLDataSource
import com.asamioffice.text.UPathString

/*
 * Dec. 17, 2003
 *
 * @since   Sep. 29, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class URLContent(aUrl: URL, aContext: GEntityContext) extends GDataSourceContent(new URLDataSource(aUrl, aContext), aContext) {
  val url: URL = aUrl

  def this(aUrl: String, aContext: GEntityContext) = this(new URL(aUrl), aContext)

  override def get_Suffix: Option[String] = {
    UPathString.getSuffix(url.toString()) match {
      case null => None
      case "" => None
      case suffix => Some(suffix)
    }
  }
}
