package org.goldenport.recorder

/*
 * Utility aspect for object itself to record something 
 *
 * Logger, Messager, Reporter
 *
 * @since   Oct. 28, 2008
 *  version Nov. 10, 2011
 *  version Feb.  1, 2012
 *  version Jun. 17, 2012
 *  version Oct. 19, 2012
 * @version Nov.  4, 2012
 * @author  ASAMI, Tomoharu
 */
trait GRecordable extends Recordable {
  final protected def setup_Recordable(aRecorder: GRecorder) {
    setup_FowardingRecorder(aRecorder)
  }

  protected final def do_e[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_error(l);Left(l)
    }
  }

  protected final def do_w[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_warning(l);Left(l)
    }
  }

  protected final def do_t[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_trace(l);Left(l)
    }
  }

  // XXX fix spec
  @deprecated("fix spec", "0.5")
  final protected def not_implemented_yet {
    sys.error("Not implmented yet (" + this + ")")
  }

  @deprecated("fix spec", "0.5")
  final protected def not_implemented_yet(anAny: Any) {
    sys.error("Not implmented yet ( " + this + ") : " + anAny)
  }
}
/*
trait GRecordable0 {
  var recorder: GRecorder = NullRecorder

  final protected def setup_Recordable(aRecorder: GRecorder) {
    require (aRecorder != null)
    require (recorder == NullRecorder)
    recorder = aRecorder
  }

  //
  def record_error(message: String, args: Any*) {
    recorder.record_error(message, args: _*)
  }

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

  final protected def record_debug(message: => String) {
    recorder.record_debug(message)
  }

  final protected def record_trace(message: => String) {
    recorder.record_trace(message)
  }

  //
  final protected def not_implemented_yet {
    sys.error("Not implmented yet (" + this + ")")
  }

  final protected def not_implemented_yet(anAny: Any) {
    sys.error("Not implmented yet ( " + this + ") : " + anAny)
  }
}
*/
