package org.goldenport.services

import scalaz._
import Scalaz._
import scala.swing._
import scala.swing.BorderPanel.Position._
import org.goldenport.service._
import org.goldenport.entity._

/**
 * @since   Feb. 11, 2012
 * @version Feb. 15, 2012
 * @author  ASAMI, Tomoharu
 */
class SwingExecutorService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  private lazy val _available_services = {
    serviceContext.serviceSpace.serviceClasses.filter(_.name != "")
  }

  private def _system_properties = {
    new GridPanel(1, 2) {
      contents += new Label("label")
      contents += new Label("value")
    }
  }

  private def _command_properties(s: GServiceClass) = {
    new GridPanel(1, 2) {
      contents += new Label("label")
      contents += new Label("value")
    }
  }

  private lazy val _main_panels = {
    for (s <- _available_services) yield {
      val n = s.name
      val p = new TabbedPane {
        pages += new TabbedPane.Page(n, _command_properties(s))
        pages += new TabbedPane.Page("System", _system_properties)
      }
      n -> p
    }
  } 

  private def _header = {
    new FlowPanel {
      contents.append(_cbox_services, _button_execute, _button_cancel)
    }
  }

  private def _button_execute = {
    new Button("Execute")
  }

  private def _button_cancel = {
    new Button("Cancel")
  }

  private def _cbox_services = {
    new ComboBox(_available_services.map(_.name))
  }

  private def _main_panel = {
    new FlowPanel {
      contents.append(_main_panels.map(_._2): _*)
    }
  }

  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) {
    val swing = new SimpleSwingApplication {
      def top = new MainFrame {
        title = "SimpleModeler"
        contents = new BorderPanel {
          add(_header, North)
          add(_main_panel, Center)
        }
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
