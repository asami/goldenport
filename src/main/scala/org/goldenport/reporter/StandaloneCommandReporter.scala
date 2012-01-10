package org.goldenport.reporter

import scala.collection.mutable.ArrayBuffer
import scala.xml.{XML, Elem, Node}
import java.io._
import org.goldenport._
import org.goldenport.container.GContainerContext
import org.goldenport.parameter._

/*
 * @since   Apr.  2, 2009
 * @version Oct. 30, 2010
 * @author  ASAMI, Tomoharu
 */
class StandaloneCommandReporter(val context: GContainerContext) extends GReporter with GoldenportConstants {
  private var _out: BufferedWriter = _
  private val _contents = new ArrayBuffer[Content]

  private def report_file_name = "report-%s.xml".format(context.startDateTimeString)
  private def report_file = {
    val dir = context.reportDirectory
    dir.mkdirs
    new File(dir, report_file_name)
  }

  override def openReporter() {
  }

  override def closeReporter() {
    val file = report_file
    _out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), context.textEncoding))
    val report = make_report()
    val formatter = new scala.xml.PrettyPrinter(80, 2)
    _out.write(formatter.format(report))
//    XML.write(_out, report, "UTF-8", false, null)
    _out.flush()
    _out.close()
    _out = null
  }

  private def parameters(keys: Seq[String]) = {
    for (key <- keys) yield <parameter name={key} category={category(key)}>{value(key)}</parameter>
  }

  private def category(key: String) = {
    context.parameters.getEntry(key) match {
      case None => "N/A"
      case Some(entry) => entry.category match {
        case Parameter_System => "system"
        case Parameter_Container => "container"
        case Parameter_Application => "application"
        case Parameter_User => "user"
        case Parameter_Startup => "startup"
        case Parameter_Command => "command"
        case _ => "?"
      }
    }
  }

  private def value(key: String) = {
    context.parameters.get(key) match {
      case None => "N/A"
      case Some(value) => value.toString
    }
  }

  private def make_report() = {
<report xmlns="http://simplemodeling.org/ns/simplemodeler/report">
  <config>
    { parameters(Array(System_Java_Version,
		       System_Java_Vendor,
		       System_Java_Vendor_url,
		       System_Java_Home,
		       System_Java_Class_Path,
		       System_Java_Library_Path,
		       System_Os_Name,
		       System_Os_Arch,
		       System_Os_Version,
		       System_Java_Io_Tmpdir,
		       System_File_Separator,
		       System_Path_Separator,
		       System_Line_Separator,
		       System_User_Name,
		       System_User_Home,
		       System_User_Dir,
		       Container_Text_Encoding,
		       Container_Text_Encoding_Alias,
		       Container_Text_Line_Separator,
		       Container_Text_Line_Separator_Alias,
		       Container_Input_Base,
		       Container_Input_Base_Alias,
		       Container_Output_Base,
		       Container_Output_Base_Alias,
		       Container_Output_Auxiliary,
		       Container_Output_Auxiliary_Alias,
		       Container_Output_Log,
		       Container_Output_Log_Alias,
		       Container_Output_Report,
		       Container_Output_Report_Alias,
		       Container_Classpath,
		       Container_Log,
		       Container_Report,
		       Application_Copyright_Years,
		       Application_Copyright_Owner,
		       Application_Name,
		       Application_Version,
		       Application_Version_Build,
		       Application_Command_Name,
		       Application_Classpath,
		       User_Classpath,
		       User_Classpath_Alias)) }
  </config>
  { make_messages }
  { make_errors }
</report>
  }

  private def make_messages() = {
    val contents = _contents.filter(_.isInstanceOf[MessageContent]).map(_.asInstanceOf[MessageContent])
    if (contents.isEmpty) Nil
    else {
<messages>
  { for (content <- contents) yield <message>{ content.message }</message> }
</messages>
    }
  }

  private def make_errors() = {
    val contents = _contents.filter(_.isInstanceOf[ErrorContent]).map(_.asInstanceOf[ErrorContent])
    if (contents.isEmpty) Nil
    else {
<errors>
  { for (content <- contents) yield <error>{ content.exception }</error> } // XXX
</errors>
    }
  }

  override def message(message: String) {
    _contents += MessageContent(message)
  }

  override def error(ex: Throwable) {
    _contents += ErrorContent(Some(ex), None)
  }

  override def error(ex: Throwable, message: String) {
    _contents += ErrorContent(Some(ex), Some(message))
  }

  override def error(message: String) {
    _contents += ErrorContent(None, Some(message))
  }

  override def warning(message: String) {
    _contents += WarningContent(message)
  }

  abstract class Content {
  }

  case class MessageContent(val message: String) extends Content {
  }

  case class ErrorContent(val exception: Option[Throwable], val message: Option[String]) extends Content {
  }

  case class WarningContent(val message: String) extends Content {
  }
}

