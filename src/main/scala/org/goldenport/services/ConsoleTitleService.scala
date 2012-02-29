package org.goldenport.services

import org.goldenport.service._
import org.goldenport.entity._
import org.goldenport.record._

/*
 * @since   Oct. 30, 2008
 *  version Jul. 15, 2010
 * @version Feb. 29, 2012
 * @author  ASAMI, Tomoharu
 */
class ConsoleTitleService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) {
    record_messageC("Copyright(c) ")
    record_messageC(application_copyright_years)
    record_messageC(" ")
    record_messageC(application_copyright_owner)
    record_messageC(". All rights reserved.")
    record_message()
    record_messageC(application_name)
    record_messageC(" Version ")
    record_messageC(application_version)
    record_messageC(" (")
    record_messageC(application_version_build)
    record_messageC(")")
    record_message()
    record_message()
    record_messageC("Usage: ")
    record_messageC(application_commnad_name)
    record_messageC(" [-options] [args...]")
    record_message()
    record_message("  for more information, use -help option")
  }
}

class ConsoleVersionService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) {
    record_messageC("Copyright(c) ")
    record_messageC(application_copyright_years)
    record_messageC(" ")
    record_messageC(application_copyright_owner)
    record_messageC(". All rights reserved.")
    record_message()
    record_messageC(application_name)
    record_messageC(" Version ")
    record_messageC(application_version)
    record_messageC(" (")
    record_messageC(application_version_build)
    record_messageC(")")
    record_message()
  }
}

class ConsoleHelpService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) {
    record_messageC("Copyright(c) ")
    record_messageC(application_copyright_years)
    record_messageC(" ")
    record_messageC(application_copyright_owner)
    record_messageC(". All rights reserved.")
    record_message()
    record_messageC(application_name)
    record_messageC(" Version ")
    record_messageC(application_version)
    record_messageC(" (")
    record_messageC(application_version_build)
    record_messageC(")")
    record_message()
    record_message()
    record_messageC("Usage: ")
    record_messageC(application_commnad_name)
    aRequest.arguments.toList match {
      case Nil => _help_index
      case x :: xs => _help_detail(x.toString)
    }
  }

  private def _help_index {
    record_messageC(" command [-options ...] [args ...]")
    record_message()
    record_message()
    for (service <- serviceContext.serviceSpace.serviceClasses
         if service.name != "") {
      record_message("- %s     \t%s", service.name, service.description.titleString)
    }
  }

  private def _help_detail(s: String) {
    serviceContext.serviceSpace.serviceClasses.find(_.name == s) match {
      case Some(service) => {
        record_message(" %s [-options ...] [args ...]".format(service.name))
        record_message(service.name)
        record_message(service.description.titleString)
        record_message(service.description.summaryString)
        record_message(service.description.contentString)
        _help_detail_contract(service.contract)
      }
      case None => _help_index
    }
  }

  private def _help_detail_contract(contract: RecordSchema) {
    for (f <- contract.fields) {
      record_message("-%s     \t%s", f.name, f.description.map(_.title.toText) getOrElse "")
    }
  }
}

object ConsoleTitleServiceClass extends GServiceClass("") {
  override protected def accept_Service_Name(aCall: GServiceCall): Option[Boolean] = {
    aCall.request.serviceName match {
      case "" => Some(true)
      case "version" => Some(true)
      case "help" => Some(true)
      case _ => None
    }
  }

  def new_Service(aCall: GServiceCall): GService = {
    aCall.request.serviceName match {
      case "" => new ConsoleTitleService(aCall, this)
      case "version" => new ConsoleVersionService(aCall, this)
      case "help" => new ConsoleHelpService(aCall, this)
      case _ => sys.error("not implemented yet")
    }
  }
}
