package org.goldenport.session

import org.goldenport.container.GContainerContext
import org.goldenport.service._
import org.goldenport.entity._

/*
 * Aug. 30, 2008
 * Nov.  3, 2008
 */
class RootSessionContext(val serviceSpace: GServiceSpace, val entitySpace: GEntitySpace, aContainerContext: GContainerContext) extends GSessionContext(aContainerContext) {
}
