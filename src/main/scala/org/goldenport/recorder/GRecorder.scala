package org.goldenport.recorder

import org.goldenport.parameter.GParameterRepository

/**
 * Provides recording facility
 *
 * Logger, Messager, Reporter
 *
 * <dl>
 *   <dt>Logger</dt>
 *   <dd>Execution log for developers.<dd>
 *   <dt>Messager</dt>
 *   <dd>Execution message for application operators.<dd>
 *   <dt>Reporter</dt>
 *   <dd>Application result report for application users.<dd>
 * </dl>
 *
 * @since   Oct. 28, 2008
 * @version Oct. 30, 2011
 * @author  ASAMI, Tomoharu
 */
trait GRecorder {
  protected final def setup_GRecorder(theParams: GParameterRepository) {
  }

  def openRecorder() {
  }

  def closeRecorder() {
  }

  /**
   * message
   * log: error
   * report
   */
  def record_error(ex: Throwable)
  def record_error(message: String, args: Any*)
  def record_error(ex: Throwable, message: String, args: Any*)

  /**
   * message
   * log: warning
   * report
   */
  def record_warning(message: String, args: Any*)

  /**
   * information for console and log
   * 
   * message
   * log: info
   */
  def record_info(message: String, args: Any*)

  /**
   * log: debug
   */
  def record_debug(message: String, args: Any*)

  /**
   * log: trace
   */
  def record_trace(message: String, args: Any*)

  /**
   * information for console message and report
   * 
   * message
   * log: info
   * report
   */
  def record_message(message: String, args: Any*)
  @deprecated
  def record_messageC(message: String, args: Any*)
  def record_message()

  /**
   * information for report only
   * 
   * log: info
   * report
   */
  def record_report(message: String, args: Any*)
}

trait NullRecorder extends GRecorder {
  override def record_error(ex: Throwable) {
  }

  override def record_error(message: String, args: Any*) {
  }

  override def record_error(ex: Throwable, message: String, args: Any*) {
  }

  override def record_warning(message: String, args: Any*) {
  }

  override def record_info(message: String, args: Any*) {
  }

  override def record_debug(message: String, args: Any*) {
  }

  override def record_trace(message: String, args: Any*) {
  }

  override def record_messageC(message: String, args: Any*) {
  }

  override def record_message(message: String, args: Any*) {
  }

  override def record_message() {
  }

  override def record_report(message: String, args: Any*) {
  }
}

object NullRecorder extends NullRecorder
