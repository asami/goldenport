package org.goldenport.services

import scala.swing._
import org.goldenport.service._
import org.goldenport.entity._

/**
 * @since   Feb. 11, 2012
 * @version Feb. 11, 2012
 * @author  ASAMI, Tomoharu
 */
class SwingExecutorService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) {
    val swing = new SimpleSwingApplication {
      def top = new MainFrame {
        title = "サンプル"
        contents = new Label("Swingの実験です！")
      }
    }
    swing.main(Array.empty)
  }
}

object SwingExecutorServiceClass extends GServiceClass("") {
  override protected def accept_Service_Name(aCall: GServiceCall): Option[Boolean] = {
    aCall.request.serviceName match {
      case "swing" => Some(true)
      case _ => None
    }
  }

  def new_Service(aCall: GServiceCall): GService = {
    aCall.request.serviceName match {
      case "swing" => new SwingExecutorService(aCall, this)
      case _ => sys.error("not implemented yet")
    }
  }
}
