package org.goldenport.logger

import java.io._
import org.goldenport.monitor.logger.GLogger
import org.goldenport.container.GContainerContext
import com.asamioffice.goldenport.util.UDateTime
import com.asamioffice.goldenport.xml.UXML

/*
 * @since   Apr.  2, 2009
 *  version Oct. 30, 2011
 * @version Jul. 21, 2012
 * @author  ASAMI, Tomoharu
 */
class StandaloneCommandLogger(val context: GContainerContext) extends GLogger {
  private var out: BufferedWriter = _
  
  private def log_file_name = "log-%s.xml".format(context.startDateTimeString)
  private def log_file = {
    val dir = context.logDirectory
    dir.mkdirs
    new File(dir, log_file_name)
  }

  override def openLogger() {
    val file = log_file
    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), context.textEncoding))
    log_println("<logs xmlns=\"http://simplemodeling.org/ns/simplemodeler/log\">")
  }

  override def closeLogger() {
    log_println("</logs>")
    out.flush()
    out.close()
    out = null
  }

  protected final def log_print(text: String) {
    out.write(text)
  }

  protected final def log_println(text: String) {
    out.write(text)
    out.newLine() // XXX
    out.flush()
  }

  protected final def log_record_exception(level: String, text: String, e: Throwable) {
    log_record_detail(level, text) {
      log_print("<exception class=\"")
      log_print(e.getClass.getName)
      log_print("\" message=\"")
      log_print(e.getMessage)
      log_println("\">")
      log_print(make_stacktrace(e))
      log_println("</exception>")
    }
  }

  protected final def log_record(level: String, text: String) {
    log_print("<log datetime=\"")
    log_print(UDateTime.getCurrentDateTimeMillXmlString)
    log_print("\" level=\"")
    log_print(level)
    log_print("\" message=\"")
    log_print(UXML.escape(text))
    log_println("\"/>")
  }

  protected final def log_record_detail(level: String, text: String)(detail: => Unit) {
    log_print("<log datetime=\"")
    log_print(UDateTime.getCurrentDateTimeMillXmlString)
    log_print("\" level=\"")
    log_print(level)
    log_print("\" message=\"")
    log_print(UXML.escape(text))
    log_println("\">")
    detail
    log_println("</log>")
  }

  override def fatal(message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def fatal(e: Throwable, message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def fatal(e: Throwable) {
    throw new Exception("")
  }

  override def error(message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def error(e: Throwable, message: String, args: AnyRef*) {
    log_record_exception("error", context.formatString(message, args: _*), e)
  }

  override def error(e: Throwable) {
    log_record_exception("error", e.getMessage, e)
  }

  override def warning(message: String, args: AnyRef*) {
    log_record("warning", context.formatString(message, args: _*))
  }

  override def warning(e: Throwable, message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def info(message: String, args: AnyRef*) {
    log_record("info", context.formatString(message, args: _*))
  }

  override def info(e: Throwable, message: String, args: AnyRef*) {
    log_record("info", context.formatString(message, args: _*))
  }

  override def config(message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def config(e: Throwable, message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def entering(instance: Any, method: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def exiting(instance: Any, method: String) {
    throw new Exception("")
  }

  override def exiting(instance: Any, method: String, result: AnyRef) {
    throw new Exception("")
  }

  override def exiting(instance: Any, method: String, e: Throwable) {
    throw new Exception("")
  }

  override def debug(message: String, args: AnyRef*) {
    log_record("debug", context.formatString(message, args: _*))
  }

  override def debug(e: Throwable, message: String, args: AnyRef*) {
    throw new Exception("")
  }

  override def trace(message: String, args: AnyRef*) {
    log_record("trace", context.formatString(message, args: _*))
  }

  override def debug_scala(message: String) {
    log_record("debug", message)
  }

  override def debug_scala(message: String, args: Array[AnyRef]) {
    log_record("debug", context.formatString(message, args: _*))
  }

  private def make_stacktrace(e: Throwable) = {
    val buffer = new StringWriter()
    val output = new PrintWriter(buffer)
    e.printStackTrace(output)
    output.flush()
    buffer.toString()
  }
}
