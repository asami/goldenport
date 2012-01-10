package org.goldenport.entity.content

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.StringDataSource
import com.asamioffice.text.UPathString

/*
 * @since   Aug. 17, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class StringContent(val string: String, aContext: GEntityContext) extends GContent(aContext) {
  private var context_bytes: Array[Byte] = _ // weak

  def this(aContext: GEntityContext)(aString: String) = this(aString, aContext)

  override def get_Suffix: Option[String] = Some("txt")

  override def open_InputStream(): InputStream = {
    new ByteArrayInputStream(get_bytes())
  }

  override def open_Reader(): Reader = {
    new StringReader(string)
  }

  override def write_OutputStream(aOut: OutputStream): Boolean = {
    aOut.write(get_bytes())
    true
  }

  private def get_bytes(): Array[Byte] = {
    if (context_bytes == null) {
      context_bytes = string.getBytes(aContext.textEncoding)
    }
    context_bytes
  }
}
