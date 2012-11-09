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
 * @version Nov.  9, 2012
 * @author  ASAMI, Tomoharu
 */
trait GRecordable extends Recordable {
  final protected def setup_Recordable(aRecorder: GRecorder) {
    setup_FowardingRecorder(aRecorder)
  }

  protected final def doe_e[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_error(l);Left(l)
    }
  }

  protected final def doe_w[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_warning(l);Left(l)
    }
  }

  protected final def doe_i[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_info(l);Left(l)
    }
  }

  protected final def doe_d[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_debug(l);Left(l)
    }
  }

  protected final def doe_t[A, B](lr: Either[String, A])(f: A => B): Either[String, B] = {
    lr match {
      case Right(r) => Right(f(r))
      case Left(l) => record_trace(l);Left(l)
    }
  }

  //
  protected final def dor_e[T](msg: String, args: Any*)(f: => T)(implicit formatter: T => String = (_: T).toString): T = {
    val r = f
    record_error(msg.format(args: _*) + " = " + formatter(r))
    r
  }

  protected final def dor_w[T](msg: String, args: Any*)(f: => T)(implicit formatter: T => String = (_: T).toString): T = {
    val r = f
    record_warning(msg.format(args: _*) + " = " + formatter(r))
    r
  }

  protected final def dor_i[T](msg: String, args: Any*)(f: => T)(implicit formatter: T => String = (_: T).toString): T = {
    val r = f
    record_info(msg.format(args: _*) + " = " + formatter(r))
    r
  }

  protected final def dor_d[T](msg: String, args: Any*)(f: => T)(implicit formatter: T => String = (_: T).toString): T = {
    val r = f
    record_debug(msg.format(args: _*) + " = " + formatter(r))
    r
  }

  protected final def dor_t[T](msg: String, args: Any*)(f: => T)(implicit formatter: T => String = (_: T).toString): T = {
    val r = f
    record_trace(msg.format(args: _*) + " = " + formatter(r))
    r
  }

  protected final def none_e[T](msg: String)(x: Option[T]): Option[T] = {
    if (x.isEmpty) {
      record_error(msg)
    }
    x
  }

  protected final def none_w[T](msg: String)(x: Option[T]): Option[T] = {
    if (x.isEmpty) {
      record_warning(msg)
    }
    x
  }

  protected final def none_i[T](msg: String)(x: Option[T]): Option[T] = {
    if (x.isEmpty) {
      record_info(msg)
    }
    x
  }

  protected final def none_d[T](msg: String)(x: Option[T]): Option[T] = {
    if (x.isEmpty) {
      record_debug(msg)
    }
    x
  }

  protected final def none_t[T](msg: String)(x: Option[T]): Option[T] = {
    if (x.isEmpty) {
      record_trace(msg)
    }
    x
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
