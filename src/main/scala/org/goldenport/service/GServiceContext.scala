package org.goldenport.service

import java.io.File
import org.goldenport.entity._
import org.goldenport.container.GContainerContext
import org.goldenport.recorder.ForwardingRecorder
import org.goldenport.parameter.GParameterRepository

/*
 * @since   Aug. 28, 2008
 * @version Jul. 31, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GServiceContext(val serviceSpace: GServiceSpace,
			       val containerContext: GContainerContext,
			       val parameters: GParameterRepository)
	 extends ForwardingRecorder {
  private var _entity_space: GEntitySpace = _
  setup_FowardingRecorder(containerContext, parameters)

  final def entitySpace: GEntitySpace = {
    require (_entity_space != null)
    _entity_space
  }

  final def setEntitySpace(aSpace: GEntitySpace) {
    require (_entity_space == null)
    _entity_space = aSpace
  }

  final def parameter[R <: Any](aKey: String): R = {
    parameters.get(aKey).get.asInstanceOf[R]
  }

  final def getParameter[R <: Any](aKey: String): Option[R] = {
    parameters.get(aKey).asInstanceOf[Option[R]]
  }

  final def newInstance[R <: Any](aName: String): R = {
    containerContext.newInstance[R](aName)
  }

  final def compileSource(file: File) {
    containerContext.compileSource(file)
  }

  final def executeScript(file: File) {
    containerContext.executeScript(file)
  }

  final def interpret(line: String) {
    containerContext.interpret(line)
  }
}
