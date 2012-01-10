package org.goldenport.entity.content

import scala.xml.{XML, Elem}
import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.locator.GLocator
import org.goldenport.entity.locator.ContentLocator
import com.asamioffice.io.UIO

/*
 * since Oct. 1, 2003
 *
 * TODO: cache, dirty working data, immutable
 *
 * @since   Aug. 10, 2008
 *  version Jul. 29, 2010
 * @version Dec. 14, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class GContent(aContext: GEntityContext) {
  val context: GEntityContext = aContext
  private var last_modified: Long = System.currentTimeMillis()
  private var open_count: Int = 0
  private var content_locator: ContentLocator = _
  private var _size_cache: Option[Long] = None

  def isCommitable: Boolean = true

  final def lastModified: Long = get_Last_Modified().getOrElse(last_modified)

  protected def get_Last_Modified(): Option[Long] = None

  final def getSuffix(): Option[String] = {
    if (!get_Suffix().isEmpty) {
      get_Suffix()
    }
    None
  }

  protected def get_Suffix(): Option[String] = {
    None
  }

  final def locator: ContentLocator = {
    if (content_locator == null) {
      content_locator = content_Locator
      if (content_locator == null) {
	content_locator = new ContentLocator(this) {}
      }
    }
    content_locator
  }

  protected def content_Locator: ContentLocator = null

  final def openCount = open_count

  final def open() {
    if (open_count == 0) {
      open_count += 1
      open_Content()
    } else {
      open_count += 1
    }
  }

  protected def open_Content(): Unit = null

  final def close() {
    open_count -= 1
    if (open_count == 0) {
      close_Content()
    }
  }

  protected def close_Content(): Unit = null

  final def openInputStream(): InputStream = {
    val in = open_InputStream()
    if (in == null) error("no InputStream")
    in
  }

  protected def open_InputStream(): InputStream = null

  final def openReader(): Reader = {
    val reader = open_Reader()
    if (reader != null) reader
    else new InputStreamReader(openInputStream, context.textEncoding)
  }

  protected def open_Reader(): Reader = null

  final def write(aDataSource: GDataSource) {
    var out: OutputStream = null
    try {
      out = aDataSource.openOutputStream()
      write(out)
    } finally {
      if (out != null) out.close()
    }
  }

  final def write(aOut: OutputStream) {
    // XXX : cache
    if (write_OutputStream(aOut)) {
      return
    }
    val in = openInputStream()
    try {
      UIO.stream2stream(in, aOut)
    } finally {
      aOut.flush()
      in.close()
    }
  }

  protected def write_OutputStream(aOut: OutputStream): Boolean = false

  /*
   * commit operation is nop in case of immutable contents
   */
  final def commit() {
  }

  final def isImmutable: Boolean = true

  // heavy weight method
  final def getSize: Long = {
    if (_size_cache.isDefined) return _size_cache.get
    val size: Long = get_Size match {
      case Some(size) => size
      case None => {
        getBinary.length
      }
    }
    _size_cache = Some(size)
    size
  }

  final def getHintSize: Option[Long] = {
    get_Size
  }

  protected def get_Size: Option[Long] = None

  final def getBinary(): Array[Byte] = {
    val binary = get_Binary() match {
      case Some(binary) => binary
      case None => {
        val in = openInputStream()
        try {
          UIO.stream2Bytes(in)
        } finally {
          in.close()
        }
      }
    }
    _size_cache = Some(binary.length)
    binary
  }

  protected def get_Binary(): Option[Array[Byte]] = None

  final def getText(): String = {
    val text = get_Text()
    if (text != null) return text
    val in = openReader()
    try {
      UIO.reader2String(in)
    } finally {
      in.close()
    }
  }

  protected def get_Text(): String = null

  final def getXml(): Elem = {
    val xml = get_Xml()
    if (xml != null) return xml
    val in = openReader()
    try {
      XML.load(in)
    } finally {
      in.close()
    }
  }

  protected def get_Xml(): Elem = null
}

object GContent {
  def create(aValue: String, aContext: GEntityContext): StringContent =
    new StringContent(aContext)(aValue)

  def create(aValue: Any, aContext: GEntityContext): GContent = {
    aValue match {
      case string: String => create(string, aContext)
      case _ => error("unimplement value = " + aValue)
    }
  }
}
