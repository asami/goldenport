package org.goldenport.session

import org.goldenport.service._
import org.goldenport.unitofwork._
import org.goldenport.entity.SubEntitySpace

/*
 * Aug. 28, 2008
 * Aug. 31, 2008
 */
class SessionServiceCallEngine(aSession: GSession, aEngine: GServiceCallEngine) {
  private val service_session = aSession
  private val service_engine = aEngine
  private val service_queue = new GServiceCallQueue()

  final def addServiceCall(aCall: GServiceCall) {
    aCall.setSession(service_session)
    service_queue.enqueue(aCall)
  }

  final def execute() {
    var call: GServiceCall = null
    var unitofwork: GUnitofwork = null
    call = service_queue.dequeue()
    while (call != null) {
      unitofwork = service_session.createUnitofwork()
      service_engine.execute(call, unitofwork)
      unitofwork.commit()
      call = service_queue.dequeue()
    }
  }

  final def execute(aCall: GServiceCall) {
    addServiceCall(aCall)
    execute()
  }
}
