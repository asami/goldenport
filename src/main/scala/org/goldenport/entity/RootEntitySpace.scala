package org.goldenport.entity

import org.goldenport.recorder.GRecorder
import org.goldenport.container.GContainerContext
import org.goldenport.parameter.GParameterRepository
import org.goldenport.entities.workspace.TreeWorkspaceEntity

/*
 * Aug. 30, 2008
 * Nov.  3, 2008
 */
class RootEntitySpace(aContainerContext: GContainerContext, theParams: GParameterRepository) extends GEntitySpace(aContainerContext, theParams) {
  val tree = new TreeWorkspaceEntity(context)
}
