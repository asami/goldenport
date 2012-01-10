package org.goldenport

import scala.collection.mutable.ArrayBuffer
import scala.tools.nsc.{ScriptRunner, GenericRunnerSettings, Interpreter}
import scala.tools.nsc.util.BatchSourceFile
import java.io.File
import java.net.{URL, URLClassLoader}
import org.goldenport.monitor._
import org.goldenport.recorder._
import org.goldenport.container.GContainerContext
import org.goldenport.session._
import org.goldenport.service._
import org.goldenport.services.ConsoleTitleServiceClass
import org.goldenport.entity._
import org.goldenport.importer._
import org.goldenport.exporter._
import org.goldenport.parameter._
import org.goldenport.application._
import com.asamioffice.util.CommandParameterParser
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.BinaryDataSource

/*
 * @since   Aug. 28, 2008
 * @version Jan. 10, 2012
 * @author  ASAMI, Tomoharu
 */
class Goldenport(theArgs: Array[String], aDesc: GApplicationDescriptor) extends GoldenportConstants {
  val version = "0.2.4"
  val build = "20120110"
  private var _system_parameters = setup_system_parameters
  private var _container_parameters = setup_container_parameters
  private var _application_parameters = setup_application_parameters(aDesc)
  private var _user_parameters = setup_user_parameters
  private val _startup_parameters = setup_startup_parameters(theArgs)
  private val _monitor: GMonitor = setup_monitor
  private val _container_context = setup_container_context
  private val entity_space = setup_entity_space
  private val service_space = setup_service_space
  private val session_space = setup_session_space
  private var _session: GSession = _

  setup_default_services
  setup_application(aDesc)

  def this(theArgs: Array[String]) = this (theArgs, NullApplicationDescriptor)

  //
  private def setup_system_parameters: GParameterRepository = {
    val params = new GParameterRepository
    params.setCategory(Parameter_System)
    params.put(System_Java_Version, System.getProperty(System_Java_Version))
    params.put(System_Java_Vendor, System.getProperty(System_Java_Vendor))
    params.put(System_Java_Vendor_url, System.getProperty(System_Java_Vendor_url))
    params.put(System_Java_Home, System.getProperty(System_Java_Home))
    params.put(System_Java_Class_Path, System.getProperty(System_Java_Class_Path))
    params.put(System_Java_Library_Path, System.getProperty(System_Java_Library_Path))
    params.put(System_Os_Name, System.getProperty(System_Os_Name))
    params.put(System_Os_Arch, System.getProperty(System_Os_Arch))
    params.put(System_Os_Version, System.getProperty(System_Os_Version))
    params.put(System_Java_Io_Tmpdir, System.getProperty(System_Java_Io_Tmpdir))
    params.put(System_File_Separator, System.getProperty(System_File_Separator))
    params.put(System_Path_Separator, System.getProperty(System_Path_Separator))
    params.put(System_Line_Separator, System.getProperty(System_Line_Separator))
    params.put(System_User_Name, System.getProperty(System_User_Name))
    params.put(System_User_Home, System.getProperty(System_User_Home))
    params.put(System_User_Dir, System.getProperty(System_User_Dir))
    params
  }

  private def setup_container_parameters: GParameterRepository = {
    val params = new GParameterRepository
    params.setParent(_system_parameters)
    params.setCategory(Parameter_Container)
    params.setAlias(Container_Text_Encoding_Alias, Container_Text_Encoding)
    params.setAlias(Container_Text_Line_Separator_Alias, Container_Text_Line_Separator)
    params.setAlias(Container_Output_Base_Alias, Container_Output_Base)
    params.setAlias(Container_Output_Auxiliary_Alias, Container_Output_Auxiliary)
    params.setAlias(Container_Output_Log_Alias, Container_Output_Log)
    params.setAlias(Container_Output_Report_Alias, Container_Output_Report)
    params.setSubstitute(Container_Output_Auxiliary, Container_Output_Base)
    params.setSubstitute(Container_Output_Log, Container_Output_Auxiliary)
    params.setSubstitute(Container_Output_Report, Container_Output_Auxiliary)
    params.put(Container_Text_Encoding, "utf-8")
    params.put(Container_Text_Line_Separator, "\n")
    params.put(Container_Input_Base, ".")
    params.put(Container_Output_Base, ".")
    params.put(Container_Log, "none")
    params.put(Container_Report, "none")
    params.put(Container_Script, "false")
    params
  }

  private def setup_application_parameters(aDesc: GApplicationDescriptor): GParameterRepository = {
    def get_classpath: String = {
      aDesc.classpath match {
	case null => ""
	case classpath => classpath.mkString(";")
      }
    }

    val params = new GParameterRepository
    params.setParent(_container_parameters)
    params.setCategory(Parameter_Application)
    params.put(Application_Name, aDesc.name)
    params.put(Application_Version, aDesc.version)
    params.put(Application_Version_Build, aDesc.version_build)
    params.put(Application_Copyright_Years, aDesc.copyright_years)
    params.put(Application_Copyright_Owner, aDesc.copyright_owner)
    params.put(Application_Command_Name, aDesc.command_name)
    params.put(Application_Classpath, get_classpath)
    params
  }

  private def setup_user_parameters: GParameterRepository = {
    val params = new GParameterRepository
    params.setParent(_application_parameters)
    params.setCategory(Parameter_User)
    params.setAlias(User_Classpath_Alias, User_Classpath)
    params
  }

  private def setup_startup_parameters(theArgs: Array[String]): GParameterRepository = {
    val params = new GParameterRepository
    params.setParent(_user_parameters)
    params.setCategory(Parameter_Startup)
    val parser = new CommandParameterParser(theArgs)
    for (option <- parser.getOptions) {
      params.put(option.getKey, option.getValue)
    }
    params
  }

  private def setup_monitor = {
    new DefaultMonitor
  }

  private def setup_container_context = {
    val context = new GoldenportContainerContext(_monitor, _startup_parameters)
    context.setRecorder(new StandaloneCommandRecorder(context))
/*
    val urls = new ArrayBuffer[URL]
    // XXX use -input option as base url for relative url.
    urls ++= _startup_parameters.getUrls(User_Classpath)
    urls ++= _startup_parameters.getUrls(Application_Classpath)
    urls ++= _startup_parameters.getUrls(Container_Classpath)
    try {
      val settings = new GenericRunnerSettings((error: String) => println(error)) // XXX error message
      val args = List("-classpath", urls.mkString(":"))
      settings.processArguments(args, true)._2
      settings.embeddedDefaults(getClass.getClassLoader) // XXX not work
      settings.nc.value = true
      settings.usejavacp.value = true
      val interpreter = new Interpreter(settings)
      context.setInterpreter(interpreter)
      context.addClassLoader(interpreter.classLoader)
    } catch {
      case e => context.addClassLoader(new URLClassLoader(urls.toArray))
    }
*/
    context
  }

  private def setup_entity_space = {
    new RootEntitySpace(_container_context, _startup_parameters)
  }

  private def setup_service_space = {
    new GServiceSpace(_container_context, _startup_parameters)
  }

  private def setup_session_space = {
    new GSessionSpace(service_space, entity_space, _container_context, _startup_parameters)
  }

  private def setup_default_services {
    addServiceClass(ConsoleTitleServiceClass)
  }

  private def setup_application(aDesc: GApplicationDescriptor) {
    aDesc.services.foreach(addServiceClass)
    aDesc.entities.foreach(addEntityClass)
    aDesc.importers.foreach(addImporterClass)
    aDesc.exporters.foreach(addExporterClass)
  }

  //
  final def addServiceClass(aClass: GServiceClass) {
    service_space.addServiceClass(aClass)
  }

  final def addEntityClass(aClass: GEntityClass) {
    entity_space.addEntityClass(aClass)
  }

  final def addImporterClass(aClass: GImporterClass) {
    service_space.addImporterClass(aClass)
  }

  final def addExporterClass(aClass: GExporterClass) {
    service_space.addExporterClass(aClass)
  }

  final def getParameter(aKey: String): Option[Any] = {
    _startup_parameters.get(aKey)
  }

  final def open() {
    _container_context.open()
    _session = session_space.createSession()
    execute_script()
  }

  private def execute_script() {
    for (filename <- getParameter("script.init")) { // XXX
      _container_context.compileSource(new File(filename.toString))
    }
  }

  final def close() {
    _container_context.close()
  }

  final def executeShellCommand[R >: AnyRef](theArgs: Array[String]): R = {
    try {
      val call = new ShellCommandServiceCall(theArgs, _startup_parameters)
      _session.execute(call)
      call.response.result match {
        case None   => None
        case result => result.asInstanceOf[R]
      }
    } catch {
      case ex: IllegalArgumentException => {
        val context = _session.sessionContext
        context.record_error(ex)
        None
      }
    }
  }

  def execute[R >: Any](service: String, args: Seq[AnyRef], props: Map[String, AnyRef] = Map.empty): Option[R] = {
    val call = new DirectServiceCall(service, args, props)
    _session.execute(call)
    call.response.resultOption
/*
    call.response.result match {
      case None   => _find_result(call.response.realms)
      case result: R => Some(result)
      case _ => None // XXX
    }
*/
  }

  def executeAsAnyRef[R >: AnyRef](service: String, args: Seq[AnyRef], props: Map[String, AnyRef] = Map.empty): Option[R] = {
    val call = new DirectServiceCall(service, args, props)
    _session.execute(call)
    call.response.resultOptionAnyRef
  }

  private def _find_result[R](realms: Seq[GTreeContainerEntity]): Option[R] = {
    val results: Seq[R] = for {
        realm <- realms
        content <- realm.collect(_.isLeaf)
    } yield content.content.asInstanceOf[R]
    if (results.isEmpty) None else Some(results.head) 
  }

  def isAccept(service: String, args: Seq[AnyRef], props: Map[String, AnyRef] = Map.empty): Boolean = {
    true
  }

  def createDataSource(name: String, data: Array[Byte], mimetype: String = null): GDataSource = {
    BinaryDataSource.createBinary(data, entity_space.context, name, mimetype)
  }
  
  class GoldenportContainerContext(aMonitor: GMonitor, theParams: GParameterRepository) extends GContainerContext(aMonitor, theParams) {
    private var _encoding: Option[String] = None
    private var _newline: Option[String] = None
    private var _interpreter: Option[Interpreter] = None

    final def setRecorder(aRecorder: GRecorder) {
      setup_FowardingRecorder(aRecorder)
    }

    final def setInterpreter(interpreter: Interpreter) {
      _interpreter = Some(interpreter)
    }

    override def open() {
      super.open()
      if (_startup_parameters.getBoolean(Container_Script)) {
        _open_interpreter()
      } else {
        _open_classloader()
      }
    }

    private def _open_interpreter() {
      val urls = new ArrayBuffer[URL]
      // XXX use -input option as base url for relative url.
      urls ++= _startup_parameters.getUrls(User_Classpath)
      urls ++= _startup_parameters.getUrls(Application_Classpath)
      urls ++= _startup_parameters.getUrls(Container_Classpath)
      try {
        val settings = new GenericRunnerSettings((error: String) => println(error)) // XXX error message
        val args = List("-classpath", urls.mkString(":"))
        settings.processArguments(args, true)._2
        settings.embeddedDefaults(getClass.getClassLoader) // XXX not work
        settings.nc.value = true
        settings.usejavacp.value = true
        val interpreter = new Interpreter(settings)
        setInterpreter(interpreter)
        addClassLoader(interpreter.classLoader)
      } catch {
        case e => addClassLoader(new URLClassLoader(urls.toArray))
      }
    }

    private def _open_classloader() {
      val urls = new ArrayBuffer[URL]
      // XXX use -input option as base url for relative url.
      urls ++= _startup_parameters.getUrls(User_Classpath)
      urls ++= _startup_parameters.getUrls(Application_Classpath)
      urls ++= _startup_parameters.getUrls(Container_Classpath)
      addClassLoader(new URLClassLoader(urls.toArray))
    }

    override final def compileSource(file: File) {
      _interpreter match {
        case Some(interpreter) => {
          val f = new scala.tools.nsc.io.PlainFile(scala.tools.nsc.io.File(file))
          interpreter.compileSources(new BatchSourceFile(f))
        }
        case None => super.compileSource(file)
      }
    }

    override final def executeScript(file: File) {
      throw new UnsupportedOperationException("executeScript")
    }

    override final def interpret(line: String) {
      _interpreter match {
        case Some(interpreter) => interpreter.interpret(line)
        case None => super.interpret(line)
      }
    }

    override protected def text_Encoding: String = {
      if (_encoding.isEmpty) {
        _startup_parameters.getString(Container_Text_Encoding) match {
          case None => return ""
          case Some("system") => throw new UnsupportedOperationException(
              "encodingのsystemはまだ実装されていません。")
          case Some(value: String) => _encoding = Some(value)
        }
      }
      _encoding.get
    }

    override protected def text_NewLine: String = {
      if (_newline.isEmpty) {
        _startup_parameters.getString(Container_Text_Line_Separator) match {
          case None => return ""
          case Some(value: String) => {
            val newline = value match {
              case "\n" => "\n"
              case "\r" => "\r"
              case "\r\n" => "\r\n"
              case "lf" => "\n"
              case "cr" => "\r"
              case "crlf" => "\r\n"
              case "system" => throw new UnsupportedOperationException(
                  "newlineのsystemはまだ実装されていません。")
              case _ => throw new IllegalArgumentException(
                  "newlineのパラメタとして「%s」が誤っています。")
            }
            _newline = Some(newline)
          }
        }
      }
      _newline.get
    }
  }
}

trait GoldenportConstants {
  val System_Java_Version = "java.version"
  val System_Java_Vendor = "java.vendor"
  val System_Java_Vendor_url = "java.vendor.url"
  val System_Java_Home = "java.home"
  val System_Java_Class_Path = "java.class.path"
  val System_Java_Library_Path = "java.library.path"
  val System_Os_Name = "os.name"
  val System_Os_Arch = "os.arch"
  val System_Os_Version = "os.version"
  val System_Java_Io_Tmpdir = "java.io.tmpdir"
  val System_File_Separator = "file.separator"
  val System_Path_Separator = "path.separator"
  val System_Line_Separator = "line.separator"
  val System_User_Name = "user.name"
  val System_User_Home = "user.home"
  val System_User_Dir = "user.dir"
  //
  val Container_Text_Encoding = "container.text.encoding"
  val Container_Text_Encoding_Alias = "encoding"
  val Container_Text_Line_Separator = "container.text.line.separator"
  val Container_Text_Line_Separator_Alias = "newline"
  val Container_Input_Base = "container.input.base"
  val Container_Input_Base_Alias = "input"
  val Container_Output_Base = "container.output.base"
  val Container_Output_Base_Alias = "output"
  val Container_Output_Auxiliary = "container.output.auxiliary"
  val Container_Output_Auxiliary_Alias = "output.aux"
  val Container_Output_Log = "container.output.log"
  val Container_Output_Log_Alias = "output.log"
  val Container_Output_Report = "container.output.report"
  val Container_Output_Report_Alias = "output.report"
  val Container_Classpath = "container.classpath"
  val Container_Log = "container.log"
  val Container_Report = "container.report"
  val Container_Script = "container.script"
  //
  val Application_Copyright_Years = "application.copyright.years"
  val Application_Copyright_Owner = "application.copyright.owner"
  val Application_Name = "application.name"
  val Application_Version = "application.version"
  val Application_Version_Build = "application.version.build"
  val Application_Command_Name = "application.command.name"
  val Application_Classpath = "application.classpath"
  //
  val User_Classpath = "user.classpath"
  val User_Classpath_Alias = "classpath"
}

object Goldenport extends GoldenportConstants {
  def main(theArgs: Array[String]) {
    val container = new Goldenport(theArgs)
    container.open()
    container.executeShellCommand(theArgs)
    container.close()
  }
}
