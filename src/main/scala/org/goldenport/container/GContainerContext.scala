package org.goldenport.container

import java.io.File
import java.net.URL
import java.util.GregorianCalendar
import org.goldenport.GoldenportConstants
import org.goldenport.monitor.{GMonitor, DefaultMonitor}
import org.goldenport.recorder.ForwardingRecorder
import org.goldenport.parameter._
import com.asamioffice.goldenport.util.ContextClassLoader

/*
 * derived from IRContainerContext.java and AbstractContainerContext.java
 * since Jul. 11, 2006
 *
 * @since   Nov.  3, 2008
 * @version Jul. 31, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GContainerContext(val monitor: GMonitor, val parameters: GParameterRepository) extends ForwardingRecorder with GoldenportConstants {
  private val _classLoader = new ContextClassLoader(monitor.getClassLoader)
  val startDateTime: GregorianCalendar = new GregorianCalendar

  def open() {
    openRecorder()
  }

  def close() {
    closeRecorder()
  }

  def startDateTimeString = {
    String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", startDateTime)
  }

  final def addClassLoader(aClassLoader: ClassLoader) {
    _classLoader.addClassLoader(aClassLoader)
  }

  final def textEncoding: String = {
    val encoding = text_Encoding
    require (encoding != null)
    if (encoding != "") encoding
    else monitor.getTextEncoding
  }

  protected def text_Encoding: String = ""

  final def newLine: String = {
    val newline = text_NewLine
    require (newline != null)
    if (newline != "") newline
    else monitor.getNewLine
  }

  protected def text_NewLine: String = ""

  final def isPlatformWindows: Boolean = {
    val mayBeWindows = is_Platform_Windows
    if (mayBeWindows.isDefined) mayBeWindows.get
    else monitor.isPlatformWindows
  }

  protected def is_Platform_Windows: Option[Boolean] = None

  final def newInstance[R <: Any](aName: String): R = {
    val clazz = _classLoader.loadClass(aName)
    clazz.newInstance.asInstanceOf[R]
  }

  final def getResource(aName: String): URL = {
    _classLoader.getResource(aName)
  }

  final def loggerKind: String = {
    parameters.get(Container_Log) match {
      case Some(kind) => kind.toString
      case _ => throw new IllegalStateException("%sに値が設定されていません。".format(Container_Log))
    }
  }

  final def reporterKind: String = {
    parameters.get(Container_Report) match {
      case Some(kind) => kind.toString
      case _ => throw new IllegalStateException("%sに値が設定されていません。".format(Container_Report))
    }
  }

  final def logDirectory: File = {
    parameters.get(Container_Output_Log) match {
      case Some(dirname) => new File(dirname.toString)
      case None => parameters.get(Container_Output_Auxiliary) match {
	case Some(dirname) => new File(dirname.toString)
	case None => parameters.get(Container_Output_Base) match {
	  case Some(dirname) => new File(dirname.toString)
	  case None => throw new InternalError("no log output directory")
	}
      }
    }
  }

  final def reportDirectory: File = {
    parameters.get(Container_Output_Report) match {
      case Some(dirname) => new File(dirname.toString)
      case None => parameters.get(Container_Output_Auxiliary) match {
	case Some(dirname) => new File(dirname.toString)
	case None => parameters.get(Container_Output_Base) match {
	  case Some(dirname) => new File(dirname.toString)
	  case None => throw new InternalError("no report output directory")
	}
      }
    }
  }

  def compileSource(file: File) {
    throw new UnsupportedOperationException("no scala execution environment")
  }

  def executeScript(file: File) {
    throw new UnsupportedOperationException("no scala execution environment")
  }

  def interpret(line: String) {
    throw new UnsupportedOperationException("no scala execution environment")
  }
}

/*
            URL RelaxerJar = UURL.getURLFromFileOrURLName(System.getProperty("java.class.path"));
            URL trangJar = UBaseFile.getURL(RelaxerJar, "trang.jar");
            URL trangAdapterJar = UBaseFile.getURL(RelaxerJar, "trangadapter.jar");
            ClassLoader loader = new URLClassLoader(
                new URL[] { trangAdapterJar, trangJar }
            );
            Class trangClass = loader.loadClass("org.relaxer.Relaxer.adapters.TrangAdapter");
            Object trang = trangClass.newInstance();

 */

object NullContainerContext extends GContainerContext(DefaultMonitor.getMonitor, NullParameterRepository)
