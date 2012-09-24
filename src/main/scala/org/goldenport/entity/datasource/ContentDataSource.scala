package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.ContentLocator
import org.goldenport.entity.content.GContent

/*
 * @since   Aug. 14, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class ContentDataSource(aContent: ContentLocator, aContext: GEntityContext) extends GDataSource(aContext, aContent) with GContentDataSource {
  def this(aContent: GContent, aContext: GEntityContext) = this(aContent.locator, aContext)

  val content: GContent = aContent.content

  override def open_InputStream(): InputStream = {
    content.openInputStream()
  }

  override def open_Reader(): Reader = {
    content.openReader()
  }
}
