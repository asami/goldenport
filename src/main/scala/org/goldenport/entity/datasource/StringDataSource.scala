package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.NameLocator

/*
 * @since   Aug.  8, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class StringDataSource(aName: NameLocator, aContext: GEntityContext)(aString: String) extends GDataSource(aName, aContext) with GContentDataSource {
  private val ds_string: String = aString

  def this(aName: String, aContext: GEntityContext)(aString: String) = this(new NameLocator(aName), aContext)(aString)

  override def open_InputStream(): InputStream = {
    new ByteArrayInputStream(get_bytes())
  }

  override def open_Reader(): Reader = {
    new StringReader(ds_string)
  }

  override protected def write_OutputStream(out: OutputStream): Boolean = {
    out.write(get_bytes())
    true
  }

  private def get_bytes() = ds_string.getBytes(textEncoding)
}
