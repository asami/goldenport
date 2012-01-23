package org.goldenport.recorder

import org.goldenport.monitor.messager.ConsoleMessager
import org.goldenport.container.GContainerContext
import org.goldenport.monitor.logger._
import org.goldenport.monitor.messager._
import org.goldenport.logger._
import org.goldenport.reporter._

/**
 * Provides recording facility
 *
 * Logger, Messager, Reporter
 *
 * @since   Apr.  2, 2009
 * @version Oct. 30, 2011
 *  version Jan. 23, 2012
 * @author  ASAMI, Tomoharu
 */
class StandaloneCommandRecorder(val context: GContainerContext) extends GRecorder {
  private var messager: GMessager = _
  private var logger: GLogger = _
  private var reporter: GReporter = _
  private var _message_level: RecorderLevel = null
  private var _logger_level: RecorderLevel = null

  override def openRecorder() {
    messager = new ConsoleMessager
    _message_level = context.messagerLevel
    _logger_level = context.loggerLevel
    logger = context.loggerKind match {
      case "none" => new NullLogger
      case _ => new StandaloneCommandLogger(context)
    }
    reporter = context.reporterKind match {
      case "none" => new NullReporter
      case _ => new StandaloneCommandReporter(context)
    }
    logger.openLogger()
    reporter.openReporter()
  }

  override def closeRecorder() {
    reporter.closeReporter()
    logger.closeLogger()
    reporter = null
    logger = null
  }

  override def record_error(ex: Throwable) {
    if (RecorderLevel.isError(_message_level)) {
      messager.errorln(ex.getMessage)
    }
    logger.error(ex)
    reporter.error(ex)
  }

  override def record_error(message: String, args: Any*) {
    val msg = message.format(args: _*)
    if (RecorderLevel.isError(_message_level)) {
      messager.errorln(msg)
    }
    logger.error(msg)
    reporter.error(msg)
  }

  override def record_error(ex: Throwable, message: String, args: Any*) {
    val msg = message.format(args: _*)
    if (RecorderLevel.isError(_message_level)) {
      messager.errorln(msg)
    }
    logger.error(ex, msg)
    reporter.error(ex, msg)
  }

  override def record_warning(message: String, args: Any*) {
    val msg = message.format(args: _*)
    if (RecorderLevel.isWarning(_message_level)) {
      messager.warningln(msg)
    }
    logger.warning(msg)
    reporter.warning(msg)
  }

  override def record_info(message: String, args: Any*) {
    val msg = message.format(args: _*)
    if (RecorderLevel.isInfo(_message_level)) {
      messager.message(msg)
    }
    logger.info(msg)
  }

  override def record_debug(message: => String) {
    if (RecorderLevel.isDebug(_message_level) ||
        RecorderLevel.isDebug(_logger_level)) {
      val msg = message
      if (RecorderLevel.isDebug(_message_level)) {
        messager.message(msg)
      }
      if (RecorderLevel.isDebug(_logger_level)) {
        logger.debug(msg)
      }
    }
  }

  override def record_trace(message: => String) {
    if (RecorderLevel.isTrace(_message_level) ||
        RecorderLevel.isTrace(_logger_level)) {
      val msg = message
      if (RecorderLevel.isTrace(_message_level)) {
        messager.message(msg)
      }
      if (RecorderLevel.isDebug(_logger_level)) {
        logger.trace(msg)
      }
    }
  }

  override def record_messageC(message: String, args: Any*) {
    val msg = message.format(args:_*)
    messager.message(msg)
    logger.info(msg)
    reporter.message(msg)
  }

  override def record_message(message: String, args: Any*) {
    val msg = message.format(args:_*)
    messager.messageln(msg)
    logger.info(msg)
    reporter.message(msg)
  }

  override def record_message() {
    messager.messageln()
    logger.info("")
  }

  override def record_report(message: String, args: Any*) {
    val msg = message.format(args: _*)
    reporter.message(msg)
  }
}
