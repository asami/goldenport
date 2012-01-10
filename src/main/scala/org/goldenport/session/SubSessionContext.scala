package org.goldenport.session

import org.goldenport.service._
import org.goldenport.entity._

/*
 * Aug. 30, 2008
 * Nov.  3, 2008
 */
class SubSessionContext(parent: GSessionContext) extends GSessionContext(parent.containerContext) {
  var entity_space: GEntitySpace = _

  def serviceSpace: GServiceSpace = parent.serviceSpace
  override def entitySpace: GEntitySpace = {
    if (entity_space != null) entity_space else parent.entitySpace
  }

  final def setEntitySpace(anEntitySpace: GEntitySpace) {
    entity_space = anEntitySpace
  }

}
