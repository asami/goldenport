package org.goldenport.session

import org.goldenport.service._
import org.goldenport.unitofwork._
import org.goldenport.entity.{GEntitySpace, SubEntitySpace}

/*
 * Aug. 28, 2008
 * Apr.  1, 2009
 */
class GSession(val sessionContext: GSessionContext) {
  private val session_entitySpace = new SubEntitySpace(sessionContext.entitySpace)
  private val session_engine = new SessionServiceCallEngine(this, sessionContext.serviceCallEngine)

  final def open() {
  }

  final def close() {
  }

  final def entitySpace: GEntitySpace = session_entitySpace

  final def createUnitofwork(): GUnitofwork = {
    new GUnitofwork()
  }

  final def execute(aCall: GServiceCall) {
    session_engine.execute(aCall)
  }
}
