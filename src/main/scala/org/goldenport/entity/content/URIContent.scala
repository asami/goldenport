package org.goldenport.entity.content

import java.net.URI
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.URIDataSource
import com.asamioffice.goldenport.text.UPathString

/*
 * Aug. 11, 2006
 *
 * @since   Sep. 29, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class URIContent(aUri: URI, aContext: GEntityContext) extends GDataSourceContent(new URIDataSource(aUri, aContext), aContext) {
  val uri: URI = aUri

  def this(aUri: String, aContext: GEntityContext) = this(new URI(aUri), aContext)

  override def get_Suffix: Option[String] = {
    UPathString.getSuffix(uri.toString()) match {
      case null => None
      case "" => None
      case suffix => Some(suffix)
    }
  }
}
