package org.goldenport.recorder

import org.goldenport.parameter.{GParameterRepository, NullParameterRepository}

/*
 * Provides recording facility
 *
 * Logger, Messager, Reporter
 *
 * @since   Oct. 29, 2008
 *  version Nov.  5, 2011
 * @version Dec.  9, 2011
 * @author  ASAMI, Tomoharu
 */
trait ForwardingRecorder extends GRecorder {
  private var _recorder: GRecorder = _

  override def openRecorder() {
    _recorder.openRecorder()
  }

  override def closeRecorder() {
    _recorder.closeRecorder()
  }

  protected final def setup_FowardingRecorder(aRecorder: GRecorder) {
    setup_FowardingRecorder(aRecorder, NullParameterRepository)
  }

  protected final def setup_FowardingRecorder(aRecorder: GRecorder, theParams: GParameterRepository) {
    require (aRecorder != null)
    require (_recorder == null)
    _recorder = aRecorder
    setup_GRecorder(theParams)
  }

  override def record_error(ex: Throwable) {
    _recorder.record_error(ex)
  }

  override def record_error(message: String, args: Any*) {
    _recorder.record_error(message, args: _*)
  }

  override def record_error(ex: Throwable, message: String, args: Any*) {
    _recorder.record_error(ex, message, args: _*)
  }

  override def record_warning(message: String, args: Any*) {
    _recorder.record_warning(message, args: _*)
  }

  override def record_info(message: String, args: Any*) {
    _recorder.record_info(message, args: _*)
  }

  override def record_debug(message: String, args: Any*) {
    _recorder.record_debug(message, args: _*)
  }

  override def record_trace(message: String, args: Any*) {
    _recorder.record_trace(message, args: _*)
  }

  override def record_messageC(message: String, args: Any*) {
    _recorder.record_messageC(message, args: _*)
  }

  override def record_message(message: String, args: Any*) {
    _recorder.record_message(message, args: _*)
  }

  override def record_message() {
    _recorder.record_message()
  }

  override def record_report(message: String, args: Any*) {
    _recorder.record_report(message, args: _*)
  }
}

trait Recordable extends ForwardingRecorder
