package org.goldenport.entities.workspace

import org.goldenport.entity.GEntity
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.GEntityContext

/*
 * Aug. 19. 2008
 * Aug. 19, 2008
 */
class WorkspaceDataSource(aEntity: GEntity, aContext: GEntityContext) extends GDataSource(aEntity.locator, aContext) { // WorkspaceEntity
}
