package org.goldenport.service

import org.goldenport.entity._
import org.goldenport.session._
import org.goldenport.parameter._

/*
 * derived from IRServiceCall.java and AbstractRServiceCall.java
 * since Jul. 25, 2006
 *
 * Aug. 28, 2008
 * Oct. 30, 2008
 */
abstract class GServiceCall {
  private var _session: GSession = _
  private var _service: GService = _
  private var _service_context: GServiceContext = _
  private var _entity_space: GEntitySpace = _
  var request: GServiceRequest = new GServiceRequest(this)
  var response: GServiceResponse = _
  private var specific_service_class: GServiceClass = _

  final def setup(aContext: GServiceContext) {
    require (aContext != null)
    require (_service == null)
    require (_service_context == null)
    require (_entity_space == null)
    _service_context = aContext
    _entity_space = aContext.entitySpace
//    request = new GServiceRequest(this)
    response = new GServiceResponse(this)
  }

  final def setup(aService: GService) {
    require(aService != null)
    require(_service == null)
    require(_service_context != null)
    require(_entity_space != null)
    _service = aService
  }

/* 2008-10-30
  final def setup(aService: GService, aContext: GServiceContext) {
    require (aService != null)
    require (aContext != null)
    require (_service == null)
    require (_service_context == null)
    require (_entity_space == null)
    _service = aService
    _service_context = aContext
    _entity_space = aContext.entitySpace
//    request = new GServiceRequest(this)
    response = new GServiceResponse(this)
    aService.setup()
  }
*/

/*
  final def setServiceContext(aContext: GServiceContext) {
    require(aContext != null)
    require(_service_context == null)
    _service_context = aContext
    _entity_space = aContext.entitySpace
  }

  final def setService(aService: GService) {
    require(aService != null)
    require(_service == null)
    require(_service_context != null)
    require(_entity_space != null)
    _service = aService
  }
*/

  final def service: GService = {
    require(_service != null)
    _service
  }

  final def serviceContext: GServiceContext = _service_context

  final def setSession(aSession: GSession) {
    require(_service == null)
    _session = aSession
  }

  final def session: GSession = {
    require(_session != null)
    _session
  }

  final def setEntitySpace(aEntitySpace: GEntitySpace) {
    require(_entity_space == null)
    _entity_space = aEntitySpace
  }

  final def entitySpace: GEntitySpace = {
    require(_session != null)
    if (_entity_space != null) _entity_space
    else _session.entitySpace
  }

  final def entityContext: GEntityContext = entitySpace.context

  final def execute() {
    _service.execute()
  }

  final def getSpecificServiceClass(): Option[GServiceClass] = {
    if (specific_service_class != null) Some(specific_service_class) else None
  }
}
