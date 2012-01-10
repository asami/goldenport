package org.goldenport.recorder

import org.goldenport.monitor.GMonitor

/*
 * Provides recording facility
 *
 * Logger, Messager, Reporter
 *
 * @since   Oct. 29, 2008
 * @version Oct. 16, 2009
 * @author  ASAMI, Tomoharu
 */
class MonitorRecorder(val monitor: GMonitor) extends GRecorder {
  val messager = monitor.getMessager
  val logger = monitor.getLogger

  override def record_error(ex: Throwable) {
    messager.errorln(ex.getMessage)
    logger.error(ex, ex.getMessage)
  }

  override def record_error(message: String, args: Any*) {
    val msg = message.format(args: _*)
    messager.errorln(msg)
    logger.error(msg)
  }

  override def record_error(ex: Throwable, message: String, args: Any*) {
    val msg = message.format(args: _*)
    messager.errorln(msg)
    logger.error(ex, msg)
  }

  override def record_warning(message: String, args: Any*) {
    val msg = message.format(args: _*)
    messager.messageln(msg)
    logger.warning(msg)
  }

  override def record_info(message: String, args: Any*) {
    val msg = message.format(args: _*)
    messager.messageln(msg)
    logger.info(msg)
  }

  override def record_debug(message: String, args: Any*) {
    logger.debug(message.format(args: _*))
  }

  override def record_trace(message: String, args: Any*) {
    logger.trace(message.format(args: _*))
  }

  override def record_messageC(message: String, args: Any*) {
    messager.message(message.format(args))
    logger.info(message.format(args: _*)) // XXX multiline
  }

  override def record_message(message: String, args: Any*) {
    messager.messageln(message.format(args))
    logger.info(message.format(args: _*))
  }

  override def record_message() {
    messager.messageln()
    logger.info("")
  }
  override def record_report(message: String, args: Any*) {
    logger.info(message.format(args: _*))
  }
}
