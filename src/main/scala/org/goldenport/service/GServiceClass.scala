package org.goldenport.service

import scalaz._
import Scalaz._
import org.goldenport.GClass
import com.asamioffice.goldenport.text.UString
import org.smartdox.DescriptionVar
import org.goldenport.record._

/*
 * Service instance which binds with ServiceCall and Unitofwork.
 *
 * @since   Aug. 28, 2008
 *  version Oct. 30, 2008
 * @version Feb. 17, 2012
 * @author  Asami, Tomoharu
 */
abstract class GServiceClass(val name: String) extends GClass {
  var description = new DescriptionVar
  var contract: RecordSchema = Schema()

  final def accept(aCall: GServiceCall): Boolean = {
    accept_Service_Name(aCall) match {
      case Some(result) => return result
      case _ =>
    }
    if (accept_service_name(aCall)) return true
    accept_Call(aCall)
  }

  protected def accept_Service_Name(aCall: GServiceCall): Option[Boolean] = None

  private def accept_service_name(aCall: GServiceCall): Boolean = {
    aCall.request.serviceName == name
  }

  protected def accept_Call(aCall: GServiceCall): Boolean = false

  final def newService(aCall: GServiceCall): GService = {
    require (aCall != null)
    new_Service(aCall) ensuring (_ != null)
  }

  final def newServiceOption(aCall: GServiceCall): Option[GService] = {
    require (aCall != null)
    if (!accept(aCall)) None
    else Some(newService(aCall))
  }

  protected def new_Service(aCall: GServiceCall): GService
}
