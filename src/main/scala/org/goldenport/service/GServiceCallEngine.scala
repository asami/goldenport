package org.goldenport.service

import org.goldenport.unitofwork._
import org.goldenport.entity._

/*
 * Aug. 28, 2008
 * Apr. 10, 2009
 */
class GServiceCallEngine(val space: GServiceSpace) {
  final def execute(aCall: GServiceCall, aUnitofwork: GUnitofwork) {
    val serviceContext = space.createServiceContext(aUnitofwork)
    serviceContext.setEntitySpace(
      if (false) new SubEntitySpace(aCall.session.entitySpace)
      else aCall.session.entitySpace)
    aCall.setup(serviceContext)
    val mayService = space.getService(aCall)
    if (mayService.isEmpty) {
      throw new IllegalArgumentException("サービス%sがみつかりません。".format(aCall.request.serviceName))
    }
/* 2008-10-30
    aCall.setServiceContext(space.createServiceContext(aUnitofwork))
    aCall.setEntitySpace(
      if (false) new SubEntitySpace(aCall.session.entitySpace)
      else aCall.session.entitySpace)
    aCall.setService(mayService.get)
*/
    val blueprints = space.getBlueprints(aCall)
    val promoters = space.getPromoters(aCall)
    val importers = space.getImporters(aCall)
    val exporters = space.getExporters(aCall)
    blueprints.foreach(_.prologue())
    importers.foreach(_.execute())
    promoters.foreach(_.prologue())
    aCall.execute()
    promoters.reverse.foreach(_.epilogue())
    exporters.reverse.foreach(_.execute())
    blueprints.reverse.foreach(_.epilogue())
  }
}
