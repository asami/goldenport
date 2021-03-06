package org.goldenport.entity.datasource

import scala.xml.{XML, Elem}
import java.io._
import java.net.{URL, URI}
import org.goldenport.util.{MimeType, UriAux}
import org.goldenport.entity._
import org.goldenport.entity.locator.{GLocator, NullLocator, NameLocator, URILocator}
import com.asamioffice.goldenport.io.UIO

/*
 * derived from IRDataSource.java and AbstractRDataSource since Aug. 12, 2005
 *
 * @since   Aug.  6, 2008
 *  version Jul. 15, 2010
 *  version Nov. 13, 2011
 *  version Jan. 20, 2012
 *  version Sep. 25, 2012
 * @version Nov. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GDataSource(
  val context: GEntityContext,
  val locator: GLocator,
  val resource: Option[UriAux] = None,
  val mimeType: Option[MimeType] = None
) {
  def this(aContext: GEntityContext) = this(aContext, NullLocator, null)
  def this(aContext: GEntityContext, locator: GLocator) = this(aContext, locator, null)
  def this(aContext: GEntityContext, name: String, mimetype: MimeType) = this(aContext, new NameLocator(name), None, Some(mimetype))
  def this(aContext: GEntityContext, uri: URI, mimetype: String) = this(aContext, new URILocator(uri), Some(UriAux(uri)), if (mimetype == null) None else Some(MimeType(mimetype)))
  def this(aContext: GEntityContext, uri: Option[URI], mimetype: Option[MimeType]) = this(aContext, uri.map(x => new URILocator(x)).getOrElse(NullLocator), uri.map(UriAux.apply), mimetype)

  private var text_encoding: String = null
  def simpleName: String = locator.simpleName
  def getSuffix(): Option[String] = locator.getSuffix()
  def getFile(): Option[File] = locator.getFile()
  def getUrl(): Option[URL] = locator.getUrl()
  def uri: URI = locator.uri

  final def textEncoding: String = {
    if (text_encoding != null) {
      text_encoding
    } else if (context.textEncoding != null) {
      context.textEncoding
    } else {
      "UTF-8"
    }
  }

  final def isExist: Boolean = {
    if (is_Exist) {
      true
    } else {
      var in: InputStream = null
      try {
        in = openInputStream()
        true
      } catch {
        case e: IOException => false
      } finally {
        if (in != null) in.close
      }
    }
  }

  protected def is_Exist: Boolean = false

  def openInputStream(): InputStream = {
    var in = open_InputStream()
    if (in != null) in
    else getUrl() match {
      case None => throw new IOException(this + ":" + locator)
      case Some(url) => url.openStream()
    }
  }

  protected def open_InputStream(): InputStream = null

  def openReader(): Reader = {
    open_Reader() match {
      case null => new InputStreamReader(openInputStream(), textEncoding)
      case reader => reader
    }
  }

  protected def open_Reader(): Reader = null

  def openBufferedReader(): BufferedReader = {
    var in: Reader = null
    try {
      in = openReader()
      if (in.isInstanceOf[BufferedReader]) in.asInstanceOf[BufferedReader]
      else new BufferedReader(in)
    } catch {
      case e: IOException => {
	if (in != null) in.close()
	throw e
      }
    }
  }

  def openWriter(mode: OutputMode): Option[Writer] = {
    open_Writer(mode) match {
      case None => {
        openOutputStream(mode).map(new OutputStreamWriter(_, textEncoding))
      }
      case Some(writer) => Some(writer)
    }
  }

  protected def open_Writer(mode: OutputMode): Option[Writer] = None

  def openBufferedWriter(mode: OutputMode): Option[BufferedWriter] = {
    var out: Writer = null
    try {
      openWriter(mode).map(x => new BufferedWriter(x))
    } catch {
      case e: IOException => {
	if (out != null) out.close()
	throw e
      }
    }
  }

  def write(mode: OutputMode, ds: GContentDataSource) {
    for (out <- ds.openOutputStream(mode)) {
      try {
        write(out)
      } finally {
        out.close
      }
    }
  }

  def write(aOut: OutputStream): Unit = {
    if (write_OutputStream(aOut)) {
      aOut.flush()
      return;
    }
    var in = openInputStream()
    try {
      UIO.stream2stream(in, aOut)
/*
      val buf = new Array[byte](8192)
      var size = 0
      while (size != -1) {
	size = in.read(buf)
	if (size > -1) out.write(buf, 0, size)
      }
      out.flush()*/
    } finally {
      in.close()
    }
  }

  protected def write_OutputStream(out: OutputStream): Boolean = false

  def openOutputStream(mode: OutputMode): Option[OutputStream] = {
    open_OutputStream(mode)
  }

  protected def open_OutputStream(mode: OutputMode): Option[OutputStream] = None

  final def loadXml(): Elem = {
    val element = load_Xml()
    if (element != null) return element
    val in = openInputStream()
    try {
      XML.load(in)
    } finally {
      in.close()
    }
  }

  protected def load_Xml(): Elem = null

  final def loadString(): String = {
    val string = load_String()
    if (string != null) return string
    val in = openReader()
    try {
      UIO.reader2String(in)
    } finally {
      in.close()
    }
  }

  protected def load_String(): String = null
}

class NullDataSource(aContext: GEntityContext) extends GDataSource(aContext) {
  def this() = this(NullEntityContext)
}

object NullDataSource extends NullDataSource

sealed trait OutputMode {
  def suffix: String
}
case object OverwriteOutput extends OutputMode {
  val suffix = "bak"
}
case object SampleOutput extends OutputMode {
  val suffix = "sample"
}
case object BackupOutput extends OutputMode {
  val suffix = "bak"
}
case object TryOutput extends OutputMode {
  val suffix = "bak"
}
case object TrySampleOutput extends OutputMode {
  val suffix = "sample"
}
