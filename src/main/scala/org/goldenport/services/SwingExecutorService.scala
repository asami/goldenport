package org.goldenport.services

import scalaz._
import Scalaz._
import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.event.SelectionChanged
import scala.swing.event.ButtonClicked
import java.util.concurrent.CopyOnWriteArrayList
import org.goldenport.service._
import org.goldenport.entity._
import org.goldenport.record._
import org.goldenport.swing._
import org.goldenport.util.ArrayMap
import org.goldenport.record.DefaultRecordContext

/**
 * @since   Feb. 11, 2012
 * @version Feb. 18, 2012
 * @author  ASAMI, Tomoharu
 */
class SwingExecutorService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) {
    val swing = new SimpleSwingApplication {
      def top = new Frame {
        title = "SimpleModeler"
        contents = new ExecutionPanel(serviceContext)
        minimumSize = new Dimension(480, 320)
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

class ExecutionPanel(val serviceContext: GServiceContext) extends BorderPanel {
  private val _available_services = {
    serviceContext.serviceSpace.serviceClasses.filter(_.name != "")
  }
  private val _default_service_name = _available_services.head.name
  private val _panels = new ArrayMap[String, Component]
  private val _main_panels = {
    for (s <- _available_services) yield {
      val n = s.name
      val p = new ServicePropertiesPanel() {
        visible = false
        addPropertySheet(n, _command_properties(s))
        addPropertySheet("System", _system_properties)
        addPropertyChangeHandler(_update_message)
      }
      _panels += n -> p
      n -> p
    }
  } 
  private val _message_panel = new Label()
  private val _execute_handlers = new CopyOnWriteArrayList[() => Unit]
  private val _cancel_handlers = new CopyOnWriteArrayList[() => Unit]

  add(_header, North)
  add(_main_panel, Center)
  add(_footer, South)
  _select_main_panel(_default_service_name)

  private def _system_properties = {
    val schema = Schema() 
    new PropertySheetPanel(schema)(DefaultSwingContext)
  }

  private def _command_properties(s: GServiceClass) = {
    new PropertySheetPanel(s.contract)(DefaultSwingContext)
  }

  private def _header = {
    new FlowPanel {
      contents.append(_cbox_services, _button_cancel, _button_execute) 
    }
  }

  private def _button_execute = {
    new Button("Execute") {
      reactions += {
        case ButtonClicked(s) => println("Button: " + s); _execute()
      }
    }
  }

  private def _button_cancel = {
    new Button("Cancel") {
      reactions += {
        case ButtonClicked(s) => println("Button: " + s); _cancel()
      }
    }
  }

  private def _cbox_services = {
    new ComboBox(_available_services.map(_.name)) {
      listenTo(selection)
      reactions += {
        case SelectionChanged(s) => {
          _select_main_panel(selection.item)
          _update_message()
        }
      }
    }
  }

  private def _select_main_panel(name: String) = {
    for ((n, c) <- _panels) {
      if (n == name) {
        c.visible = true
      } else {
        c.visible = false
      }
    }
  }

  private def _main_panel = {
    new FlowPanel {
      contents.append(_main_panels.map(_._2): _*)
    }
  }

  private def _footer = {
    _message_panel
  }

  private def _update_message() {
    for ((n, p) <- _current_service_panel) {
      val as = p.getArguments()
      val al = if (as.isEmpty) "" else as.mkString(" ")
      val ps = p.getPropertySeq()
      val pl = ps.map{case (k, v) => "-%s:%s".format(k, v)}.mkString(" ")
      val msg = "sm -%s%s %s".format(n, al, pl)
      _message_panel.text = "Command: " + msg
    }
  }

  private def _current_service_panel: Option[(String, ServicePropertiesPanel)] = {
    _main_panels.collectFirst {
      case (n, p) if p.visible => (n, p)
    }
  }

  def addExecutionHandler(h: () => Unit) {
    _execute_handlers.add(h)
  }

  private def _execute() {
    for (h <- _execute_handlers) {
      h.apply()
    }
  }

  def addCancelHandler(h: () => Unit) {
    _cancel_handlers.add(h)
  }

  private def _cancel() {
    for (h <- _cancel_handlers) {
      h.apply()
    }    
  }
}

class ServicePropertiesPanel extends TabbedPane {
  private val _sheets = new ArrayMap[String, PropertySheetPanel]
  private val _handlers = new CopyOnWriteArrayList[() => Unit]

  def addPropertySheet(name: String, sheet: PropertySheetPanel) {
    sheet.addPropertyChangeHandler(_fire)
    _sheets += name -> sheet
    pages += new TabbedPane.Page(name, sheet)
  }

  def getArguments(): List[String] = Nil
  def getPropertySeq(): Seq[(String, String)] = {
    _sheets.foldRight(nil[(String, String)]) { (e, a) =>
      val (_, p) = e
      p.getPropertyStringList() ::: a
    }
  }

  private def _fire() {
    for (h <- _handlers) {
      h.apply()
    }
  }

  def addPropertyChangeHandler(handler: () => Unit) {
    _handlers.add(handler)
  }
}
