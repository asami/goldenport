package org.goldenport.importer

import org.goldenport.service._
import org.goldenport.recorder.GRecordable
import org.goldenport.entity.GEntity

/*
 * @since   Aug. 29, 2008
 *  version Sep. 18, 2011
 * @version Dec. 17, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GImporter(val serviceCall: GServiceCall) extends GRecordable {
  val request = serviceCall.request
  val serviceContext = serviceCall.serviceContext
  val entityContext = serviceCall.entityContext
  setup_Recordable(serviceContext)

  final protected def reconstitute_entity(anAny: Any): Option[GEntity] = {
    serviceCall.entitySpace.reconstitute(anAny)
  }

  final protected def reconstitute_or_new_entity(anAny: Any): Option[GEntity] = {
    serviceCall.entitySpace.reconstitute_or_new(anAny)
  }

  final protected def new_instance[R <: AnyRef](aName: String): R = {
    serviceContext.newInstance[R](aName)
  }

  final def execute(): Unit = {
    execute_Import()
  }

  protected def execute_Import(): Unit
}
