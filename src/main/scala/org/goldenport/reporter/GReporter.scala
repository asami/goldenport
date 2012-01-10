package org.goldenport.reporter

/*
 * @since   Apr.  2, 2009
 * @version Oct. 10, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class GReporter {
  def openReporter() {
  }

  def closeReporter() {
  }

  def error(ex: Throwable)
  def error(message: String)
  def error(ex: Throwable, message: String)
  def warning(message: String)
  def message(message: String)
}

class NullReporter extends GReporter {
  override def error(ex: Throwable) {
  }
  
  override def error(message: String) {
  }

  override def error(ex: Throwable, message: String) {
  }

  override def warning(message: String) {
  }

  override def message(message: String) {
  }
}
object NullReporter extends NullReporter
