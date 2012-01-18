package org.goldenport.entity

import java.io.File
import java.net.URL
import java.net.URI
import scala.collection.mutable.ArrayBuffer
import org.goldenport.container.GContainerContext
import org.goldenport.parameter._
import org.goldenport.entity.datasource._
import org.goldenport.entity.content._
import org.goldenport.entity.locator.ResourceLocator
import com.asamioffice.goldenport.io.UFile

/*
 * derived from SubRModelContext.java since Aug. 3, 2005.
 *
 * @since   Feb. 28, 2009
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GSubEntityContext(val parentContext: GEntityContext, theParams: GParameterRepository) extends GEntityContext {

  def this(aParent: GEntityContext) = this(aParent, NullParameterRepository)

  final def textEncoding: String = {
    text_Encoding match {
      case Some(encoding) => encoding
      case None => parentContext.textEncoding
    }
  }

  protected def text_Encoding: Option[String] = None

  final def newLine: String = {
    text_Line match {
      case Some(line) => line
      case None => parentContext.newLine
    }
  }

  protected def text_Line: Option[String] = None

  def newInstance[R <: Any](aName: String): R = {
    parentContext.newInstance(aName)
  }

  def isPlatformWindows: Boolean = {
    parentContext.isPlatformWindows
  }

  def lookupResource(aLocator: ResourceLocator): URL = {
    parentContext.lookupResource(aLocator)
  }

  def reconstitute(aNode: GContainerEntityNode): Option[GEntity] = {
    parentContext.reconstitute(aNode)
  }

  def createWorkFile(): File = {
    parentContext.createWorkFile
  }

  def createWorkDirectory(): File = {
    parentContext.createWorkDirectory
  }

  def workDirectory: File = {
    parentContext.workDirectory
  }

  def tmpDirectory: File = {
    parentContext.tmpDirectory
  }

  def executeCommand(aCommandLine: String): Process = {
    parentContext.executeCommand(aCommandLine)
  }

  final def executeCommand(aCommandLine: String, envp: Array[String]): Process = {
    parentContext.executeCommand(aCommandLine, envp)
  }

  final def executeCommand(aCommandLine: String, envp: Array[String], dir: File): Process = {
    parentContext.executeCommand(aCommandLine, envp, dir)
  }

  def makeReferenceContent(aReference: String): GContent = {
    parentContext.makeReferenceContent(aReference)
  }

  def normalizeFilename(aName: String): String = {
    parentContext.normalizeFilename(aName)
  }
}
