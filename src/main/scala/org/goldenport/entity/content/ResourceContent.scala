package org.goldenport.entity.content

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.ResourceDataSource
import com.asamioffice.text.UPathString

/*
 * @since   Sep. 28, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class ResourceContent(val pathname: String, aContext: GEntityContext) extends GDataSourceContent(new ResourceDataSource(pathname, aContext), aContext) {

//  println("ResourceContent = " + pathname) 2008-10-31
  if (pathname.startsWith("resource:")) error(pathname)

  override def get_Suffix: Option[String] = {
    UPathString.getSuffix(pathname) match {
      case null => None
      case "" => None
      case suffix => Some(suffix)
    }
  }
}
