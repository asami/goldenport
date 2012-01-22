package org.goldenport.recorder

/*
 * Utility aspect for object itself to record something 
 *
 * Logger, Messager, Reporter
 *
 * @since   Oct. 28, 2008
 * @version Nov. 10, 2011
 * @author  ASAMI, Tomoharu
 */
trait GRecordable {
  var recorder: GRecorder = NullRecorder

  final protected def setup_Recordable(aRecorder: GRecorder) {
    require (aRecorder != null)
    require (recorder == NullRecorder)
    recorder = aRecorder
  }

  //
  def record_error(e: Throwable, message: String, args: Any*) { 
    recorder.record_error(e, message, args: _*)
  }
  
  final protected def record_messageC(message: String, args: AnyRef*) {
    recorder.record_messageC(message, args: _*)
  }

  final protected def record_message(message: String, args: AnyRef*) {
    recorder.record_message(message, args: _*)
  }

  final protected def record_message() {
    recorder.record_message()
  }

  final protected def record_debug(message: String, args: AnyRef*) {
    recorder.record_debug(message, args: _*)
  }

  //
  final protected def not_implemented_yet {
    sys.error("Not implmented yet (" + this + ")")
  }

  final protected def not_implemented_yet(anAny: Any) {
    sys.error("Not implmented yet ( " + this + ") : " + anAny)
  }
}
