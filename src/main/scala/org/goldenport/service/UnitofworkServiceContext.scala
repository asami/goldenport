package org.goldenport.service

import org.goldenport.unitofwork._
import org.goldenport.recorder.GRecorder
import org.goldenport.parameter.GParameterRepository

/*
 * Aug. 29, 2008
 * Nov.  4, 2008
 */
class UnitofworkServiceContext(val unitofwork:GUnitofwork, val parent: GServiceContext, theParams: GParameterRepository) extends GServiceContext(parent.serviceSpace, parent.containerContext, theParams) {
  
}
