package org.goldenport.recorder

import scalaz._
import Scalaz._

/**
 * @since   Jan. 23, 2012
 * @version Jan. 23, 2012
 * @author  ASAMI, Tomoharu
 */
sealed abstract class RecorderLevel
object ErrorLevel extends RecorderLevel
object WarningLevel extends RecorderLevel
object InfoLevel extends RecorderLevel
object DebugLevel extends RecorderLevel
object TraceLevel extends RecorderLevel

object RecorderLevel {
  def create(level: String): Option[RecorderLevel] = {
    level.toLowerCase match {
      case "error" => ErrorLevel.some
      case "warning" => WarningLevel.some
      case "info" => InfoLevel.some
      case "debug" => DebugLevel.some
      case "trace" => TraceLevel.some
      case _ => none
    }
  }

  def isError(level: RecorderLevel): Boolean = {
    true
  }

  def isWarning(level: RecorderLevel): Boolean = {
    level match {
      case ErrorLevel => false
      case WarningLevel => true
      case InfoLevel => true
      case DebugLevel => true
      case TraceLevel => true
      case _ => false
    }
  }

  def isInfo(level: RecorderLevel): Boolean = {
    level match {
      case ErrorLevel => false
      case WarningLevel => false
      case InfoLevel => true
      case DebugLevel => true
      case TraceLevel => true
      case _ => false
    }
  }

  def isDebug(level: RecorderLevel): Boolean = {
    level match {
      case ErrorLevel => false
      case WarningLevel => false
      case InfoLevel => false
      case DebugLevel => true
      case TraceLevel => true
      case _ => false
    }
  }

  def isTrace(level: RecorderLevel): Boolean = {
    level match {
      case ErrorLevel => false
      case WarningLevel => false
      case InfoLevel => false
      case DebugLevel => false
      case TraceLevel => true
      case _ => false
    }
  }
}
