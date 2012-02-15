package org.goldenport.services

import org.goldenport.service._
import org.goldenport.entity._

/*
 * @since   Oct. 30, 2008
 * @version Jul. 15, 2010
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
    record_messageC(" [-options] [args...]")
    record_message()
    record_message()
    for (service <- serviceContext.serviceSpace.serviceClasses
	       if service.name != "") {
      record_message("- %s     \t%s", service.name, service.summary)
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
