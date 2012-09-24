package org.goldenport.entities.workspace

import org.goldenport.entity.GEntity
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.GEntityContext

/*
 * @since   Aug. 19. 2008
 *  version Aug. 19, 2008
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class WorkspaceDataSource(aEntity: GEntity, aContext: GEntityContext) extends GDataSource(aContext, aEntity.locator) { // WorkspaceEntity
}
