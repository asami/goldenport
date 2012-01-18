package org.goldenport.entity.content

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.FileDataSource
import com.asamioffice.goldenport.text.UPathString

/*
 * Original since Jan.  3, 2004
 *
 * @since   Aug. 13, 2008
 * @version Sep. 29, 2008
 * @author  ASAMI, Tomoharu
 */
class FileContent(aFile: File, aContext: GEntityContext) extends GDataSourceContent(new FileDataSource(aFile, aContext), aContext) {
  val file: File = aFile

  def this(aFileName: String, aContext: GEntityContext) = this(new File(aFileName), aContext)

  override def get_Suffix: Option[String] = {
    UPathString.getSuffix(file.toString()) match {
      case null => None
      case "" => None
      case suffix => Some(suffix)
    }
  }
}
