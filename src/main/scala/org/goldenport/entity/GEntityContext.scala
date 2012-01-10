package org.goldenport.entity

import java.net.URL
import java.io.File
import org.goldenport.entity.locator.ResourceLocator
import org.goldenport.entity.content.GContent
import org.goldenport.container.{GContainerContext, NullContainerContext}
import org.goldenport.recorder.{NullRecorder, ForwardingRecorder}
import org.goldenport.parameter._

/*
 * derived from IRModelContext.java and AbstractRModelContext.java
 * since Aug. 2, 2005.
 *
 * @since   Aug.  5, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GEntityContext extends ForwardingRecorder {
  def textEncoding: String
  def newLine: String
  def newInstance[R <: Any](aName: String): R
  def isPlatformWindows: Boolean
  def lookupResource(aLocator: ResourceLocator): URL
  def reconstitute(aNode: GContainerEntityNode): Option[GEntity]
  def createWorkFile(): File
  def createWorkDirectory(): File
  def workDirectory: File
  def tmpDirectory: File
  def executeCommand(aCommandLine: String): Process
  def executeCommand(aCommandLine: String, envp: Array[String]): Process
  def executeCommand(aCommandLine: String, envp: Array[String], dir: File): Process
  def makeReferenceContent(aReference: String): GContent
  def normalizeFilename(aName: String): String
}

class NullEntityContext extends GEntityContext with NullRecorder {
  def textEncoding: String = {
    throw new UnsupportedOperationException("unsupported")
  }

  def newLine: String = {
    throw new UnsupportedOperationException("unsupported")
  }

  def newInstance[R <: Any](aName: String): R = {
    throw new UnsupportedOperationException("unsupported")
  }

  def isPlatformWindows: Boolean = {
    throw new UnsupportedOperationException("unsupported")
  }

  def lookupResource(aLocator: ResourceLocator): URL = {
    throw new UnsupportedOperationException("unsupported")
  }

  def reconstitute(aNode: GContainerEntityNode): Option[GEntity] = {
    throw new UnsupportedOperationException("unsupported")
  }

  def createWorkFile(): File = {
    throw new UnsupportedOperationException("unsupported")
  }

  def createWorkDirectory(): File = {
    throw new UnsupportedOperationException("unsupported")
  }

  def workDirectory: File = {
    throw new UnsupportedOperationException("unsupported")
  }

  def tmpDirectory: File = {
    throw new UnsupportedOperationException("unsupported")
  }

  def executeCommand(aCommandLine: String): Process = {
    throw new UnsupportedOperationException("unsupported")
  }

  def executeCommand(aCommandLine: String, envp: Array[String]): Process = {
    throw new UnsupportedOperationException("unsupported")
  }

  def executeCommand(aCommandLine: String, envp: Array[String], dir: File): Process = {
    throw new UnsupportedOperationException("unsupported")
  }

  def makeReferenceContent(aReference: String): GContent = {
    throw new UnsupportedOperationException("unsupported")
  }

  def normalizeFilename(aName: String): String = {
    throw new UnsupportedOperationException("unsupported")
  }
}

object NullEntityContext extends NullEntityContext
