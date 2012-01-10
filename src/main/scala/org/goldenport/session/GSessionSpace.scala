package org.goldenport.session

import org.goldenport.container.GContainerContext
import org.goldenport.parameter.GParameterRepository
import org.goldenport.service._
import org.goldenport.entity._

/*
 * derived from RSessionSpace.java since Jul. 11, 2006
 *
 * Aug. 28, 2008
 * Nov.  3, 2008
 */
class GSessionSpace(val serviceSpace: GServiceSpace,
		    val entitySpace: GEntitySpace,
		    val containerContext: GContainerContext,
		    val parameters: GParameterRepository) {
  val context = new RootSessionContext(serviceSpace, entitySpace, containerContext)

  final def createSession(): GSession = {
    val newContext = new SubSessionContext(context)
    newContext.setEntitySpace(new SubEntitySpace(entitySpace))
    val session = new GSession(newContext)
    session
  }
}
