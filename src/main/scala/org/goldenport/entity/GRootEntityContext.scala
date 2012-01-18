package org.goldenport.entity

import java.io.File
import java.net.URL
import java.net.URI
import scala.collection.mutable.ArrayBuffer
import org.goldenport.container.GContainerContext
import org.goldenport.parameter.GParameterRepository
import org.goldenport.entity.datasource._
import org.goldenport.entity.content._
import org.goldenport.entity.locator.ResourceLocator
import com.asamioffice.goldenport.io.UFile

/*
 * derived from RootRModelContext.java since Feb. 5, 2006
 *
 * @since   Feb. 28, 2009
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GRootEntityContext(val containerContext: GContainerContext, theParams: GParameterRepository) extends GEntityContext {
  private var context_tmp_directory: File = _
  private var context_goldenport_work_directory: File = _
  private var context_work_directory: File = _
  setup_FowardingRecorder(containerContext, theParams)

  final def textEncoding: String = {
    text_Encoding match {
      case Some(encoding) => encoding
      case None => containerContext.textEncoding
    }
  }

  protected def text_Encoding: Option[String] = None

  final def newLine: String = {
    new_Line match {
      case Some(nl) => nl
      case None => containerContext.newLine
    }
  }

  protected def new_Line: Option[String] = None

  final def newInstance[R <: Any](aName: String): R = {
    containerContext.newInstance[R](aName)
  }

  final def isPlatformWindows: Boolean = containerContext.isPlatformWindows

  final def lookupResource(aLocator: ResourceLocator): URL = {
    val name = if (aLocator.resourceName.startsWith("/")) {
      aLocator.resourceName.substring(1)
    } else {
      aLocator.resourceName
    }
    containerContext.getResource(name)
  }

  final def createWorkFile(): File = {
    val file = File.createTempFile("goldenport", ".tmp", workDirectory)
    file.deleteOnExit()
    file
  }

  final def createWorkDirectory(): File = {
    val dir = File.createTempFile("goldenport", ".d.tmp", workDirectory)
    dir.delete()
    dir.mkdir()
    dir.deleteOnExit()
    dir
  }

  final def workDirectory: File = {
    if (context_work_directory == null) {
      context_work_directory =
	UFile.createTempDir("goldenport", workRootDirectory)
    }
    context_work_directory
  }

  private def workRootDirectory: File = {
    if (context_goldenport_work_directory == null) {
      context_goldenport_work_directory = new File(tmpDirectory, "goldenport.d")
      if (context_goldenport_work_directory.exists() &&
	  !context_goldenport_work_directory.isDirectory()) {
	throw new IllegalStateException(
	  "%sがディレクトリでありませんでした。".format(context_goldenport_work_directory.toString))
      }
      if (!context_goldenport_work_directory.exists()) {
	context_goldenport_work_directory.mkdir()
      }
    }
    context_goldenport_work_directory
  }

  final def tmpDirectory: File = {
    if (context_tmp_directory == null) {
      val name = System.getProperty("java.io.tmpdir")
      if (name == null) {
	throw new IllegalStateException("java.io.tmpdirが設定されていません。")
      }
      context_tmp_directory = new File(name)
      if (!context_tmp_directory.isDirectory()) {
	throw new IllegalStateException(
	  "tmpdirの値%sがディレクトリでありません。".format(context_tmp_directory.toString))
      }
    }
    context_tmp_directory
  }

  final def executeCommand(aCommandLine: String): Process = {
    Runtime.getRuntime().exec(aCommandLine)
  }

  final def executeCommand(aCommandLine: String, envp: Array[String]): Process = {
    Runtime.getRuntime().exec(aCommandLine, envp)
  }

  final def executeCommand(aCommandLine: String, envp: Array[String], dir: File): Process = {
    Runtime.getRuntime().exec(aCommandLine, envp, dir)
  }

  final def normalizeFilename(aName: String): String = {
    new String(aName.filter(!is_remove_char(_)).map(convert_char).toArray)
  }

  private def is_remove_char(c: Char): Boolean = {
    c == ' '
  }

  private def convert_char(c: Char): Char = {
    c match {
      case ' ' => '_'
      case _ => c
    }
  }
}
