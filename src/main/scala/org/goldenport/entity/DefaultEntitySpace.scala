package org.goldenport.entity

import org.goldenport.container.DefaultContainerContext
import org.goldenport.parameter.DefaultParameterRepository
import org.goldenport.entities.workspace.TreeWorkspaceEntity

/*
 * Oct. 29, 2008
 * Nov.  3, 2008
 */
class DefaultEntitySpace extends RootEntitySpace(new DefaultContainerContext, new DefaultParameterRepository)
