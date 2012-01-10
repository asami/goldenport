package org.goldenport.importer

import org.goldenport.service._
import org.goldenport.entity.GEntity

/*
 * derived from DefaultImporter.java since Dec. 22, 2003
 *
 * @since   Aug. 29, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class DefaultImporter(aCall: GServiceCall) extends GImporter(aCall) {
  override def execute_Import(): Unit = {
    val args = request.arguments.map(reconstitute_entity)
    if (args.isEmpty) return
    if (!args.forall(_.isDefined)) return
    request.setEntity(args(0).get)
    request.setEntities(args.map(_.get))
  }
}
